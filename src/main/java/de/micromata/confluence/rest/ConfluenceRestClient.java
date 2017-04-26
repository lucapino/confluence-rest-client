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
package de.micromata.confluence.rest;

import de.micromata.confluence.rest.client.ContentClient;
import de.micromata.confluence.rest.client.SearchClient;
import de.micromata.confluence.rest.client.SpaceClient;
import de.micromata.confluence.rest.client.UserClient;
import de.micromata.confluence.rest.core.ContentClientImpl;
import de.micromata.confluence.rest.core.SearchClientImpl;
import de.micromata.confluence.rest.core.SpaceClientImpl;
import de.micromata.confluence.rest.core.UserClientImpl;
import de.micromata.confluence.rest.core.domain.UserBean;
import de.micromata.confluence.rest.core.misc.SecurityException;
import de.micromata.confluence.rest.core.misc.RestParamConstants;
import de.micromata.confluence.rest.core.misc.RestPathConstants;
import java.net.HttpURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client to interact with Confluence server's REST API.
 *
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class ConfluenceRestClient implements RestPathConstants, RestParamConstants {

    private final Logger log = LoggerFactory.getLogger(ConfluenceRestClient.class);

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    protected final ExecutorService executorService;

    private URI confluenceBaseUri;
    private URI restApiBaseUri;
    private String username = StringUtils.EMPTY;
    private CloseableHttpClient httpclient;
    private HttpHost proxy;
    private CookieStore cookieStore = new BasicCookieStore();
    private HttpClientContext clientContext;

    private UserClient userClient;

    private SpaceClient spaceClient;

    private ContentClient contentClient;

    private SearchClient searchClient;

    public ConfluenceRestClient(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void connect(URI uri, String username, String password) throws URISyntaxException, SecurityException {
        connect(uri, username, password, null);
    }

    public void connect(URI uri, String username, String password, HttpHost proxyHost) throws URISyntaxException, SecurityException {
        connect(uri, username, password, proxyHost, false);
    }

    public void connect(URI uri, String username, String password, HttpHost proxyHost, boolean explicit) throws URISyntaxException, SecurityException {
        log.info("Setting up REST client:");
        this.confluenceBaseUri = uri;
        this.restApiBaseUri = buildRestApiBaseURI(uri);
        log.info("  URI: " + this.restApiBaseUri);
        this.proxy = proxyHost;
        this.username = username;
        // Authentication
        log.info("  Using Basic Authentiction for user " + username);
        HttpHost target = getHttpHost(uri);
        log.debug("  Using credentials provider " + BasicCredentialsProvider.class);
        log.debug("  Authentication scope is " + target);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(username, password));
        // Create AuthCache instance
        log.debug("  Using AuthCache " + BasicAuthCache.class);
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        // Add AuthCache to the execution context
        this.clientContext = HttpClientContext.create();
        this.clientContext.setAuthCache(authCache);
        // Client
        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider);
        // Proxy
        if (this.proxy != null) {
            log.info("  Using proxy: " + this.proxy);
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(this.proxy);
            clientBuilder.setRoutePlanner(routePlanner);
        } else {
            log.info("  No proxy specified");
        }
        this.httpclient = clientBuilder.build();
        // Check validity of credentials by user profile request
        if (explicit) {
            explicitlyCheckCredentials();
        }
    }

    private void explicitlyCheckCredentials() throws URISyntaxException, SecurityException {
        Future<UserBean> future = getUserClient().getCurrentUser();
        try {
            UserBean user = future.get();
            // If current user differs from the one to login, something went wrong
            if (!this.username.equals(user.getUsername())) {
                String message = "Authentication confirms unexpected user. Expected=%1$s, confirmed=%2$s";
                throw new SecurityException(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized", String.format(message, this.username, user.getUsername()), null);
            }
        } catch (ExecutionException | InterruptedException ee) {
            if (ee.getCause() != null && ee.getCause() instanceof SecurityException) {
                throw (SecurityException) ee.getCause();
            } else {
                String message = "Authentication failed for user " + this.username + ". See cause";
                throw new SecurityException(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized", message, ee.getCause());
            }
        }
    }

    public UserClient getUserClient() {
        if (userClient == null) {
            userClient = new UserClientImpl(this, executorService);
        }
        return userClient;
    }

    public SpaceClient getSpaceClient() {
        if (spaceClient == null) {
            spaceClient = new SpaceClientImpl(this, executorService);
        }
        return spaceClient;
    }

    public ContentClient getContentClient() {
        if (contentClient == null) {
            contentClient = new ContentClientImpl(this, executorService);
        }
        return contentClient;
    }

    public SearchClient getSearchClient() {
        if (searchClient == null) {
            searchClient = new SearchClientImpl(this, executorService);
        }
        return searchClient;
    }

    /**
     * Extract port from URL
     *
     * @param endpointUrl
     * @return
     */
    private int getPort(URL endpointUrl) {
        int port = (endpointUrl.getPort() != -1 ? endpointUrl.getPort() : endpointUrl.getDefaultPort());
        if (port != -1) {
            return port;
        }
        if (HTTPS.equals(endpointUrl.getProtocol())) {
            return 443;
        }
        return 80;
    }

    private HttpHost getHttpHost(URI uri) throws URISyntaxException {
        try {
            String host = uri.getHost();
            int port = getPort(uri.toURL());
            String scheme = HTTP;
            if (port == 443) {
                scheme = HTTPS;
            }
            return new HttpHost(host, port, scheme);
        } catch (MalformedURLException e) {
            log.debug("Caught " + e + ". Transforming and throwing " + URISyntaxException.class);
            throw new URISyntaxException(uri.toString(), "URI is not a valid URL: " + e.getMessage(), 0);
        }
    }

    private URI buildRestApiBaseURI(URI uri) throws URISyntaxException {
        String path = uri.getPath();
        if (path.isEmpty() == false) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.concat(BASE_REST_PATH);
        } else {
            path = BASE_REST_PATH;
        }
        return new URIBuilder(uri).setPath(path).build();
    }

    public URI getRestApiBaseUri() {
        return restApiBaseUri;
    }

    public URI getConfluenceBaseUri() {
        return confluenceBaseUri;
    }

    public String getUsername() {
        return username;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public HttpClientContext getClientContext() {
        return clientContext;
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }
}
