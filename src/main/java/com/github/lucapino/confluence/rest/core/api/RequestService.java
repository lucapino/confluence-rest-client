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
package com.github.lucapino.confluence.rest.core.api;

import java.io.InputStream;
import java.net.URI;

/**
 * Supports making requests to a REST API.
 *
 * @author Martin Böhmer
 */
public interface RequestService {

    <T> T executeGetRequest(URI uri, Class<T> resultClass) throws RequestException;

    InputStream executeGetRequestForDownload(URI uri) throws RequestException;

    <T> T executePostRequest(URI uri, Object content, Class<T> resultClass) throws RequestException;
    
    <T> T executePutRequest(URI uri, Object content, Class<T> resultClass) throws RequestException;

    <T> T executePostRequestForUpload(URI uri, InputStream inputStream, String title, String comment,
            Class<T> resultClass) throws RequestException;

}
