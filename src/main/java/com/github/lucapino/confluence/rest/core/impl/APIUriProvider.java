/**
 * Copyright 2017 Martin Böhmer
 * Modifications Copyright 2017 Luca Tagliani
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
package com.github.lucapino.confluence.rest.core.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lucapino.confluence.rest.core.api.misc.RestPathConstants;

/**
 * @author Martin Böhmer
 */
public class APIUriProvider {

    private static final Logger LOG = LoggerFactory.getLogger(APIUriProvider.class);

    private final URI baseUri;
    private final URI restApiBaseUri;

    public APIUriProvider(URI uri) throws URISyntaxException {
        Validate.notNull(uri);
        this.baseUri = uri;
        this.restApiBaseUri = buildRestApiBaseURI(uri);
        LOG.info("  URI: {}", this.restApiBaseUri);
    }

    private URI buildRestApiBaseURI(URI uri) throws URISyntaxException {
        String path = uri.getPath();
        if (path.isEmpty() == false) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.concat(RestPathConstants.BASE_REST_PATH);
        } else {
            path = RestPathConstants.BASE_REST_PATH;
        }
        return new URIBuilder(uri).setPath(path).build();
    }

    public URI getRestApiBaseUri() {
        return restApiBaseUri;
    }

    public URI getBaseUri() {
        return baseUri;
    }

}
