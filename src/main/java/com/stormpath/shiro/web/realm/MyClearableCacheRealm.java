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
package com.stormpath.shiro.web.realm;

import com.stormpath.shiro.realm.ApplicationRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * This realm is a sub-class of {@link ApplicationRealm} and offers the possibility to external clients to
 * clear the authorization cache info from the cache. It thus forces the authorization/permissions information
 * to be re-retrieved from its original location allowing re-calculation of authorization/permission when
 * changes in the application's permissions (e.g. <a href="http://docs.stormpath.com/java/product-guide/#custom-data">Stormpath's custom data</a>)
 * has taken place.
 *
 */
public class MyClearableCacheRealm extends ApplicationRealm {

    /**
     * Clears the cached authorization information of the given principals. It forces the realm to re-retrieve the
     * authorization information from its original location.
     *
     * @param principals the principal whose authorization information shall be cleared
     */
    public void clearAuthorizationCacheInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

}
