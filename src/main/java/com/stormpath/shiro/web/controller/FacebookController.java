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

import com.stormpath.sdk.provider.CreateProviderRequest;
import com.stormpath.sdk.provider.Providers;
import com.stormpath.shiro.web.utils.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facebook-specific Singleton {@link ProviderController}.
 *
 * @since 1.0.0
 */
public class FacebookController extends ProviderController {

    private static final Logger logger = LoggerFactory.getLogger(FacebookController.class);

    private static ProviderController controller = null;

    /**
     * Let's make the constructor private so we can have a single FacebookController instance.
     */
    private FacebookController() {
    }

    public static ProviderController getInstance() {
        if(controller == null) {
            controller = new FacebookController();
        }
        return controller;
    }

    /**
     * Returns a new Facebook-based directory creation request. See
     * <a href="http://docs.stormpath.com/java/product-guide/#creating-a-facebook-directory">Creating a facebook directory</a>
     * @return a new Facebook-based directory creation request.
     */
    @Override
    public CreateProviderRequest getCreateProviderRequest() {
        //AppID and AppSecret are retrieved from the configuration file.
        return Providers.FACEBOOK.builder()
                .setClientId(Configuration.getFacebookAppId())
                .setClientSecret(Configuration.getFacebookAppSecret())
                .build();
    }

    @Override
    public String getProviderId() {
        return "facebook";
    }
}
