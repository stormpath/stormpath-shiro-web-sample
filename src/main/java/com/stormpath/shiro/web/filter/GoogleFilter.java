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
package com.stormpath.shiro.web.filter;

import com.stormpath.shiro.realm.authc.GoogleAuthenticationToken;
import com.stormpath.shiro.web.controller.GoogleController;
import com.stormpath.shiro.web.controller.ProviderController;
import com.stormpath.shiro.web.utils.Constants;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GoogleFilter extends OauthFilter {
    private static final Logger logger = LoggerFactory.getLogger(GoogleFilter.class);

    @Override
    protected AuthenticationToken getOauthAuthenticatingFilter(String code) {
        return new GoogleAuthenticationToken(code);
    }

    @Override
    protected ProviderController getProviderController() {
        return GoogleController.getInstance();
    }

    @Override
    protected String getGenericErrorMessage() {
        return Constants.GOOGLE_LOGIN_ERROR;
    }

}
