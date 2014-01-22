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
import com.stormpath.shiro.realm.ApplicationRealm;
import com.stormpath.shiro.web.model.CustomDataBean;
import com.stormpath.shiro.web.model.CustomDataFieldBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Singleton service encapsulating Custom Data operations (i.e. retrieve, delete, insert) to be executed in
 * the <a href="http://www.stormpath.com">Stormpath</a> account by means of the
 * <a href="https://github.com/stormpath/stormpath-sdk-java">Stormpath Java SDK</a>
 */
public class StormpathService {

    private static final Logger logger = LoggerFactory.getLogger(StormpathService.class);

    private static StormpathService stormpathService = null;
    private static final Client client = ((ApplicationRealm)((RealmSecurityManager) SecurityUtils.
            getSecurityManager()).getRealms().iterator().next()).getClient();

    private static final String hrefBase = "https://api.stormpath.com/v1/";

    /**
     * Let's make the constructor private so we can have a singleton.
     */
    private StormpathService() {
    }

    public static StormpathService getInstance() {
        if(stormpathService == null) {
            stormpathService = new StormpathService();
        }
        return stormpathService;
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
            customDataFieldBeanList.add(new CustomDataFieldBean(entry.getKey(), entry.getValue().toString()));
        }

        CustomDataBean customDataBean = new CustomDataBean();
        customDataBean.setCustomDataFields(customDataFieldBeanList);
        return customDataBean;
    }

    /**
     *  This method will insert a custom data field into the custom data of the given account.
     *
     * @param accountId the id of the account where the key-value field will be added.
     * @param key the key of the custom data field to be added
     * @param value the value of the custom data field to be added
     */
    public void addCustomDataField(String accountId, String key, String value) {
        if (accountId == null || accountId.length() == 0) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        if (key == null || key.length() == 0 || key.contains(" ")) {
            throw new IllegalArgumentException("key cannot be null, empty or contain spaces");
        }

        CustomData customData = client.getResource(getCustomDataHref(accountId), CustomData.class);
        customData.put(key, value);
        customData.save();
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
    }

    private static String getCustomDataHref(String accountId) {
        return hrefBase + "accounts/" + accountId + "/customData";
    }

}
