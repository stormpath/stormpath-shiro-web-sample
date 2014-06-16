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

import com.stormpath.shiro.web.controller.ProviderController;
import com.stormpath.shiro.web.utils.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class OauthFilter extends PassThruAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(OauthFilter.class);

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        String code = WebUtils.getCleanParam(request, "code");
        if(code != null && isAccessOK(code)) {
            WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
            return false;
        }
        //If we are here is very likely that the Provider-based Directory has not yet being created in Stormpath
        //Let's make developer's life easier and let's create it automatically for him...
        if (tryToCreateStormpathProviderDirectory() && isAccessOK(code)) {
            WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
            return false;
        }
        //The Provider-based directory already existed in the Stormpath Application as an account store but the attempt to login with
        //it did not succeed. There is nothing else we can do, there is indeed an error trying to login via this Oauth provider.
        Map<String, String> failureMap = new HashMap<String, String>();
        failureMap.put(Constants.MESSAGE_FLAG, getGenericErrorMessage());
        WebUtils.issueRedirect(request, response, getLoginUrl(), failureMap);
        return true;
    }

    private boolean isAccessOK(String code) {
        try {
            SecurityUtils.getSubject().login(getOauthAuthenticatingFilter(code));
            return true;
        } catch (AuthenticationException e) {
            logger.debug("The Oauth user cannot access this application.");
        }
        return false;
    }

    private boolean tryToCreateStormpathProviderDirectory() {
        return getProviderController().createProviderDirectory();
    }

    protected abstract AuthenticationToken getOauthAuthenticatingFilter(String code);

    protected abstract ProviderController getProviderController();

    protected abstract String getGenericErrorMessage();

}
