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
package com.stormpath.shiro.web.model;

/**
 * Bean holding the key-value information of a single custom data field.
 */
public class CustomDataFieldBean {

    private String key;
    private Object value;

    public CustomDataFieldBean(String aKey, Object aValue) {
        this.key = aKey;
        this.value = aValue;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
