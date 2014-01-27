/*
 * Copyright 2013 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stormpath.shiro.web.controller;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.shiro.authz.CustomDataPermissionsEditor;
import com.stormpath.shiro.realm.AccountCustomDataPermissionResolver;
import com.stormpath.shiro.web.model.CustomDataBean;
import com.stormpath.shiro.web.model.CustomDataFieldBean;
import com.stormpath.shiro.web.realm.MyClearableCacheRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Singleton controller encapsulating Custom Data operations (i.e. retrieve, delete, insert) to be executed in
 * the <a href="http://www.stormpath.com">Stormpath</a> account by means of the
 * <a href="https://github.com/stormpath/stormpath-sdk-java">Stormpath Java SDK</a>
 */
public class CustomDataController {

    private static final Logger logger = LoggerFactory.getLogger(CustomDataController.class);

    private static CustomDataController customDataController = null;
    private static final MyClearableCacheRealm realm = ((MyClearableCacheRealm)((RealmSecurityManager) SecurityUtils.
            getSecurityManager()).getRealms().iterator().next());
    private static final Client client = realm.getClient();

    private static final String hrefBase = "https://api.stormpath.com/v1/";

    /**
     * Let's make the constructor private so we can have a singleton.
     */
    private CustomDataController() {
    }

    public static CustomDataController getInstance() {
        if(customDataController == null) {
            customDataController = new CustomDataController();
        }
        return customDataController;
    }

    /**
     * Method that returns the custom data for the given account.
     *
     * @param accountId the accountId whose custom data is being requested
     * @return a {@link CustomDataBean} instance containing the custom data fields
     */
    public CustomDataBean getCustomData(String accountId) {
        if (accountId == null || accountId.length() == 0) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        CustomData customData = client.getResource(getCustomDataHref(accountId), CustomData.class);
        List<CustomDataFieldBean> customDataFieldBeanList = new ArrayList<CustomDataFieldBean>();

        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            customDataFieldBeanList.add(new CustomDataFieldBean(entry.getKey(), entry.getValue()));
        }

        CustomDataBean customDataBean = new CustomDataBean();
        customDataBean.setCustomDataFields(customDataFieldBeanList);
        return customDataBean;
    }

    /**
     *  This method will insert or update a custom data field into the custom data of the given account. When a new
     *  field is added (i.e, not previously existing key) then the field is created with the key and the value as String.
     *  If the key already exists, the existing value will be converted to a list and the new value will be added to it.
     *
     *  If the key equals the <a href="https://github.com/stormpath/stormpath-shiro/wiki#permissions)">shiro permission string</a>
     *  ('apacheShiroPermissions' by default) then {@link CustomDataPermissionsEditor} will be used to update it.
     *
     * @param accountId the id of the account where the key-value field will be added.
     * @param key the key of the custom data field to be added
     * @param value the value of the custom data field to be added
     * @return a {@link CustomDataFieldBean} instance containing the custom data field just updated
     */
    public CustomDataFieldBean addCustomDataField(String accountId, String key, String value) {
        if (accountId == null || accountId.length() == 0) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        if (key == null || key.length() == 0 || key.contains(" ")) {
            throw new IllegalArgumentException("key cannot be null, empty or contain spaces");
        }

        CustomData customData = client.getResource(getCustomDataHref(accountId), CustomData.class);

        if(isPermissionKey(key)) {
            CustomDataPermissionsEditor customDataPermissionsEditor = new CustomDataPermissionsEditor(customData);
            customDataPermissionsEditor.append(value);
            customData.save();
            invalidateAuthorizationCache(accountId);

        } else {
            Object existingValue = customData.get(key);
            if (existingValue == null) {
                customData.put(key, value);
            } else if (existingValue instanceof String) {
                Set<String> set = new LinkedHashSet<String>();
                set.add((String)existingValue);
                set.add(value);
                customData.put(key, set);
            } else if (existingValue instanceof List) {
                ((List) existingValue).add(value);
                customData.put(key, existingValue);
            } else {
                String msg = "Unable to recognize CustomData field '" + key + "' value of type " +
                            value.getClass().getName() + ".  Expected type: String or List<String>";
                throw new IllegalArgumentException(msg);
            }
            customData.save();

        }

        return new CustomDataFieldBean(key, customData.get(key));
    }

    /**
     * This method will delete a custom data field from the given account
     *
     * @param accountId the id of the account where the custom data field will be deleted
     * @param key the key of the custom data field to be deleted
     */
    public void deleteCustomDataField(String accountId, String key) {
        if (accountId == null || accountId.length() == 0) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key cannot be null or empty");
        }

        CustomData customData = client.getResource(getCustomDataHref(accountId), CustomData.class);
        customData.remove(key);
        customData.save();

        invalidateAuthorizationCache(accountId);
    }

    private static String getCustomDataHref(String accountId) {
        return getAccountHref(accountId) + "/customData";
    }

    private static String getAccountHref(String accountId) {
        return hrefBase + "accounts/" + accountId;
    }

    /**
     * Checks whether the given key equals the "shiro permission string". See: <a href="https://github.com/stormpath/stormpath-shiro/wiki#permissions></a>
     * @param key the key to compare
     * @return 'true' if the given key equals the shiro permission string. 'False' otherwise.
     */
    private boolean isPermissionKey(String key) {
        return ((AccountCustomDataPermissionResolver)realm.getAccountPermissionResolver()).getCustomDataFieldName().equals(key);
    }

    /**
     * Invalidates the authorization cache so the changes in Custom Data are caught by Shiro.
     * @param accountId the account whose authorization data will be removed from the cache.
     */
    private void invalidateAuthorizationCache(String accountId) {
        realm.getCacheManager().getCache("com.stormpath.sdk.account.Account").remove(getAccountHref(accountId));
        realm.getCacheManager().getCache("com.stormpath.sdk.directory.CustomData").remove(getCustomDataHref(accountId));
        realm.clearAuthorizationCacheInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
