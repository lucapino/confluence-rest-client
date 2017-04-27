/**
 * Copyright 2016 Micromata GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.itboehmer.confluence.rest.client;

import de.itboehmer.confluence.rest.core.domain.UserBean;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 01.07.2016
 * Project: ConfluenceTransferPlugin
 */
public interface UserClient {

    /**
     * Get the confluence user by username (example: admin)
     *
     * @param username the username
     * @return Future with the UserBean
     */
    Future<UserBean> getUserByUsername(String username) throws URISyntaxException;

    /**
     * Get the confluence user by key (example: 402880824ff933a4014ff9345d7c0002)
     *
     * @param key the key
     * @return Future with the UserBean
     */
    Future<UserBean> getUserByKey(String key) throws URISyntaxException;

    /**
     * Get the current logged in user
     *
     * @return Future with the UserBean
     */
    Future<UserBean> getCurrentUser() throws URISyntaxException;

    /**
     * Get the Anonymous User
     *
     * @return Future with the UserBean
     */
    Future<UserBean> getAnonymousUser() throws URISyntaxException;
}
