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
package de.micromata.confluence.rest.core;

import de.micromata.confluence.rest.ConfluenceRestClient;
import de.micromata.confluence.rest.client.UserClient;
import de.micromata.confluence.rest.core.domain.UserBean;
import de.micromata.confluence.rest.core.util.HttpMethodFactory;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class UserClientImpl extends BaseClient implements UserClient {
    
    private final Logger log = LoggerFactory.getLogger(UserClientImpl.class);

    public UserClientImpl(ConfluenceRestClient confluenceRestClient, ExecutorService executorService) {
        super(confluenceRestClient, executorService);
    }

    @Override
    public Future<UserBean> getUserByUsername(String username) throws URISyntaxException {
        log.info("Getting user by name. Username=" + username);
        Validate.notNull(username);
        URIBuilder uriBuilder = buildPath(USER);
        uriBuilder.addParameter(USERNAME, username);
        return getUser(uriBuilder);

    }

    @Override
    public Future<UserBean> getUserByKey(String key) throws URISyntaxException {
        log.info("Getting user by key. Key=" + key);
        Validate.notNull(key);
        URIBuilder uriBuilder = buildPath(USER);
        uriBuilder.addParameter(KEY, key);
        return getUser(uriBuilder);
    }

    @Override
    public Future<UserBean> getCurrentUser() throws URISyntaxException {
        log.info("Getting curent user");
        URIBuilder uriBuilder = buildPath(USER, CURRENT);
        return getUser(uriBuilder);
    }

    @Override
    public Future<UserBean> getAnonymousUser() throws URISyntaxException {
        log.info("Getting anonymous user");
        URIBuilder uriBuilder = buildPath(USER, ANONYMUS);
        return getUser(uriBuilder);
    }

    private Future<UserBean> getUser(URIBuilder uriBuilder) {
        return executorService.submit(() -> {
            HttpGet method = HttpMethodFactory.createGetMethod(uriBuilder.build());
            return executeRequest(method, UserBean.class);
        });
    }
}
