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
package com.stormpath.shiro.web.utils;

import com.stormpath.sdk.lang.Assert;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.Ini;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static final String CONFIG_INI_FILE = "config.ini";

    private static final Ini ini = new Ini();

    static{
        init();
    }

    private static void init() {
        InputStream inputStream = null;
        try {
            inputStream = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_INI_FILE);
            ini.load(inputStream);
            if (CollectionUtils.isEmpty(ini)) {
                logger.warn("Configuration INI resource '" + CONFIG_INI_FILE + "' exists, but it did not contain any data.");
            }
        } catch (ConfigurationException io) {
            logger.error(io.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        validate();
    }

    //Google
    public static boolean isGoogleEnabled() {
        return Boolean.parseBoolean(ini.getSection("Social").get("enableGoogle"));
    }

    public static String getGoogleClientId() {
        return GOOGLE().get("clientId");
    }

    public static String getGoogleClientSecret() {
        return GOOGLE().get("clientSecret");
    }

    public static String getGoogleRedirectUri() {
        return GOOGLE().get("redirectUri");
    }

    //Facebook
    public static boolean isFacebookEnabled() {
        return Boolean.parseBoolean(ini.getSection("Social").get("enableFacebook"));
    }

    public static String getFacebookAppId() {
        return FACEBOOK().get("appId");
    }

    public static String getFacebookAppSecret() {
        return FACEBOOK().get("appSecret");
    }

    public static String getFacebookRedirectUri() {
        return FACEBOOK().get("redirectUri");
    }

    public static String getFacebookScope() {
        return FACEBOOK().get("scope");
    }

    private static Ini.Section GOOGLE() {
        return ini.getSection("Google");
    }

    private static Ini.Section FACEBOOK() {
        return ini.getSection("Facebook");
    }

    private static void validate() {
        Assert.notNull(isGoogleEnabled());
        Assert.notNull(isFacebookEnabled());
        if(isGoogleEnabled()) {
            Assert.hasText(getGoogleClientId());
            Assert.hasText(getGoogleClientSecret());
            Assert.hasText(getGoogleRedirectUri());
        }
        if(isFacebookEnabled()) {
            Assert.hasText(getFacebookAppId());
            Assert.hasText(getFacebookAppSecret());
            Assert.hasText(getFacebookRedirectUri());
            Assert.hasText(getFacebookScope());
        }
    }


}
