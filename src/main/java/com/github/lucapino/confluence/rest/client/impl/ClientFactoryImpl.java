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

import java.util.concurrent.ExecutorService;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.ContentClient;
import com.github.lucapino.confluence.rest.client.api.SearchClient;
import com.github.lucapino.confluence.rest.client.api.SpaceClient;
import com.github.lucapino.confluence.rest.client.api.UserClient;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

public final class ClientFactoryImpl implements ClientFactory {

    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

    public ClientFactoryImpl(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        this.executorService = executorService;
        this.requestService = requestService;
        this.apiConfig = apiConfig;
    }

    @Override
    public UserClient getUserClient() {
        return new UserClientImpl(executorService, requestService, apiConfig);
    }

    @Override
    public SpaceClient getSpaceClient() {
        return new SpaceClientImpl(executorService, requestService, apiConfig);
    }

    @Override
    public ContentClient getContentClient() {
        return new ContentClientImpl(executorService, requestService, apiConfig);
    }

    @Override
    public SearchClient getSearchClient() {
        return new SearchClientImpl(executorService, requestService, apiConfig);
    }
}
