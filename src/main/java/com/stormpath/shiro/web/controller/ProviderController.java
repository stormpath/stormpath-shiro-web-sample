/*
 * Copyright 2014 Stormpath, Inc.
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

import com.stormpath.sdk.application.AccountStoreMapping;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.directory.AccountStore;
import com.stormpath.sdk.directory.CreateDirectoryRequest;
import com.stormpath.sdk.directory.Directories;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.provider.CreateProviderRequest;
import com.stormpath.sdk.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public abstract class ProviderController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    public boolean createProviderDirectory() {
        Application application = client.getResource(realm.getApplicationRestUrl(), Application.class);

        for(AccountStoreMapping accountStoreMapping : application.getAccountStoreMappings()) {
            AccountStore accountStore = accountStoreMapping.getAccountStore();
            if(accountStore.getHref().contains("directories")) {
                if(((Directory)accountStore).getProvider().getProviderId().equals(getProviderId())) {
                    return false;
                }
            }
        }

        Directory directory = client.instantiate(Directory.class);
        //Let's create a unique Dir name to avoid name conflicts when creating the dir in Stormpath.
        directory.setName(application.getName() + "-" + getProviderId().toUpperCase() + "-" + UUID.randomUUID());
        CreateDirectoryRequest request = Directories.newCreateRequestFor(directory).forProvider(getCreateProviderRequest()).build();

        Tenant tenant = client.getCurrentTenant();
        tenant.createDirectory(request);

        //Now that we have a directory, we'll map it to our application so it is active.
        AccountStoreMapping accountStoreMapping = client.instantiate(AccountStoreMapping.class);
        accountStoreMapping.setAccountStore(directory);
        accountStoreMapping.setApplication(application);
        accountStoreMapping.setListIndex(Integer.MAX_VALUE);
        accountStoreMapping.setDefaultAccountStore(false);
        accountStoreMapping.setDefaultGroupStore(false);

        application.createAccountStoreMapping(accountStoreMapping);

        return true;
    }

    public abstract CreateProviderRequest getCreateProviderRequest();

    public abstract String getProviderId();

}
