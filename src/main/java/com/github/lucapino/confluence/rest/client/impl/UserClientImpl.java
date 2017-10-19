/**
 * Copyright 2016 Micromata GmbH
 * Modifications Copyright 2017 Martin Böhmer
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
package com.github.lucapino.confluence.rest.client.impl;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucapino.confluence.rest.client.api.UserClient;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.UserBean;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;
import com.github.lucapino.confluence.rest.core.api.misc.RestParamConstants;
import com.github.lucapino.confluence.rest.core.api.misc.RestPathConstants;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public class UserClientImpl extends BaseClientImpl implements UserClient {

    private final Logger log = LoggerFactory.getLogger(UserClientImpl.class);

    public UserClientImpl(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        super(executorService, requestService, apiConfig);
    }

    @Override
    public Future<UserBean> getUserByUsername(String username) throws URISyntaxException {
        log.info("Getting user by name '{}'.", username);
        Validate.notNull(username);
        URIBuilder uriBuilder = buildPath(RestPathConstants.USER);
        uriBuilder.addParameter(RestParamConstants.USERNAME, username);
        return getUser(uriBuilder);

    }

    @Override
    public Future<UserBean> getUserByKey(String key) throws URISyntaxException {
        log.info("Getting user by key '{}'.", key);
        Validate.notNull(key);
        URIBuilder uriBuilder = buildPath(RestPathConstants.USER);
        uriBuilder.addParameter(RestParamConstants.KEY, key);
        return getUser(uriBuilder);
    }

    @Override
    public Future<UserBean> getCurrentUser() throws URISyntaxException {
        log.info("Getting current user");
        URIBuilder uriBuilder = buildPath(RestPathConstants.USER, RestPathConstants.CURRENT);
        return getUser(uriBuilder);
    }

    @Override
    public Future<UserBean> getAnonymousUser() throws URISyntaxException {
        log.info("Getting anonymous user");
        URIBuilder uriBuilder = buildPath(RestPathConstants.USER, RestPathConstants.ANONYMOUS);
        return getUser(uriBuilder);
    }

    private Future<UserBean> getUser(URIBuilder uriBuilder) {
        return executorService.submit(() -> {
            return executeGetRequest(uriBuilder.build(), UserBean.class);
        });
    }
}
