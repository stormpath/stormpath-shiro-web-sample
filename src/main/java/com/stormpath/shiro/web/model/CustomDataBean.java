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

import java.util.ArrayList;
import java.util.List;

/**
 * Bean containing all the <a href="http://docs.stormpath.com/java/product-guide/#custom-data">Custom Data</a> fields
 * structured as a list of {@link CustomDataFieldBean}s
 *
 */
public class CustomDataBean {

    private List<CustomDataFieldBean> customDataFields = new ArrayList<CustomDataFieldBean>();

    public List<CustomDataFieldBean> getCustomDataFields() {
        return customDataFields;
    }

    public void setCustomDataFields(List<CustomDataFieldBean> customDataFields) {
        if (customDataFields == null) {
            throw new IllegalArgumentException("customDataFields cannot be null");
        }
        this.customDataFields = customDataFields;
    }

}
