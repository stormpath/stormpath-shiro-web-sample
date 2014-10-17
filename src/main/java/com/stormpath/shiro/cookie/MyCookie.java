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
package com.stormpath.shiro.cookie;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyCookie extends org.apache.shiro.web.servlet.SimpleCookie {

    @Override
    public String readValue(HttpServletRequest request, HttpServletResponse ignored) {
        String value = super.readValue(request, ignored);
        if (value == null) {
            try {
                int cookieIdPos = ((ShiroHttpServletRequest) request).getRequestURI().indexOf("JSESSIONID=");
                if (cookieIdPos != -1) {
                    value = ((ShiroHttpServletRequest) request).getRequestURI().substring(((ShiroHttpServletRequest) request).getRequestURI().indexOf("JSESSIONID=") + 11, ((ShiroHttpServletRequest) request).getRequestURI().length());
                }
            } catch (Throwable t) {
                //
            }
        }
        return value;
    }
}
