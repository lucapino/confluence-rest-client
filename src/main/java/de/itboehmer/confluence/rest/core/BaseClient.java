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
package de.itboehmer.confluence.rest.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.itboehmer.confluence.rest.ConfluenceRestClient;
import de.itboehmer.confluence.rest.core.custom.CqlSearchResultDeserializer;
import de.itboehmer.confluence.rest.core.domain.cql.CqlSearchResult;
import de.itboehmer.confluence.rest.core.misc.SecurityException;
import de.itboehmer.confluence.rest.core.misc.RestException;
import de.itboehmer.confluence.rest.core.misc.RestParamConstants;
import de.itboehmer.confluence.rest.core.misc.RestPathConstants;
import de.itboehmer.confluence.rest.core.util.URIHelper;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public abstract class BaseClient implements RestParamConstants, RestPathConstants {

    private final Logger log = LoggerFactory.getLogger(BaseClient.class);

    protected final ConfluenceRestClient confluenceRestClient;
    protected final CloseableHttpClient client;
    protected final HttpClientContext clientContext;
    protected final URI baseUri;
    protected final ExecutorService executorService;

    protected Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(CqlSearchResult.class, new CqlSearchResultDeserializer())
            .create();

    public BaseClient(ConfluenceRestClient confluenceRestClient, ExecutorService executorService) {
        this.confluenceRestClient = confluenceRestClient;
        this.clientContext = confluenceRestClient.getClientContext();
        this.client = confluenceRestClient.getHttpclient();
        this.baseUri = confluenceRestClient.getRestApiBaseUri();
        this.executorService = executorService;
    }

    protected JsonReader toJsonReader(InputStream inputStream)
            throws UnsupportedEncodingException {
        Validate.notNull(inputStream);
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);
        return jsonReader;
    }

    protected URIBuilder buildPath(String... paths) {
        return URIHelper.buildPath(baseUri, paths);
    }

    protected URIBuilder buildNonRestPath(String... paths) {
        return URIHelper.buildPath(this.confluenceRestClient.getConfluenceBaseUri(), paths);
    }

    protected JsonReader getJsonReader(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        return toJsonReader(inputStream);
    }

    protected <T> T executeRequest(HttpRequestBase httpRequest, Class<T> resultClass) throws IOException, RestException {
        log.debug("Executing request " + httpRequest);
        CloseableHttpResponse response = this.client.execute(httpRequest, this.clientContext);
        int statusCode = response.getStatusLine().getStatusCode();
        log.debug("Received status code " + statusCode + " from " + httpRequest);
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                log.debug("Tranforming result into " + resultClass);
                JsonReader jsonReader = getJsonReader(response);
                T result = gson.fromJson(jsonReader, resultClass);
                httpRequest.releaseConnection();
                return result;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new SecurityException(response);
            default:
                throw new RestException(response);
        }
    }

    protected InputStream executeRequest(HttpRequestBase httpRequest) throws IOException, RestException {
        log.debug("Executing request " + httpRequest);
        CloseableHttpResponse response = this.client.execute(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        log.debug("Received status code " + statusCode + " from " + httpRequest);
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                log.debug("Transforming result into " + InputStream.class);
                return response.getEntity().getContent();
            case HttpURLConnection.HTTP_UNAUTHORIZED:
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new SecurityException(response);
            default:
                throw new RestException(response);
        }
    }

}
