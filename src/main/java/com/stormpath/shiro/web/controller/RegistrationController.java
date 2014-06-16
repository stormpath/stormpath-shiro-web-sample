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

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountStatus;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.lang.Assert;
import com.stormpath.shiro.web.model.RegistrationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private static RegistrationController controller = null;

    /**
     * Let's make the constructor private so we can have a single RegistrationController.
     */
    private RegistrationController() {
    }

    public static RegistrationController getInstance() {
        if(controller == null) {
            controller = new RegistrationController();
        }
        return controller;
    }

    public void createAccount(RegistrationBean registrationBean) {
        Assert.notNull(registrationBean, "registrationBean cannot be null.");

        Application application = client.getResource(realm.getApplicationRestUrl(), Application.class);
        Account account = client.instantiate(Account.class);
        account.setEmail(registrationBean.getEmail());
        account.setUsername(registrationBean.getUsername());
        account.setPassword(registrationBean.getPassword());
        account.setGivenName(registrationBean.getFirstName());
        account.setMiddleName(registrationBean.getMiddleName());
        account.setSurname(registrationBean.getLastName());
        account.setStatus(AccountStatus.valueOf(registrationBean.getStatus()));
        if(registrationBean.isSuppressVerificationEmail()) {
            application.createAccount(Accounts.newCreateRequestFor(account).setRegistrationWorkflowEnabled(false).build());
        } else {
            application.createAccount(account);
        }
    }

}
