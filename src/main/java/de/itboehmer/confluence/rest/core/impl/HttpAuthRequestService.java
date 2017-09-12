package de.itboehmer.confluence.rest.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;

import de.itboehmer.confluence.rest.core.RequestException;
import de.itboehmer.confluence.rest.core.RequestService;
import de.itboehmer.confluence.rest.core.misc.RestException;
import de.itboehmer.confluence.rest.core.misc.SecurityException;
import de.itboehmer.confluence.rest.core.util.HttpMethodFactory;

/**
 * {@link RequestService} implementation based on basic authentication.
 */
public class HttpAuthRequestService extends AbstractRequestService implements RequestService {

	private static final Logger log = LoggerFactory.getLogger(HttpAuthRequestService.class);

	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	private CloseableHttpClient httpclient;
	private HttpHost proxy;
	private HttpClientContext clientContext;

	public void connect(URI uri, String username, String password)
			throws URISyntaxException, SecurityException {
		connect(uri, username, password, null);
	}
	
	public void connect(URI uri, String username, String password, HttpHost proxyHost)
			throws URISyntaxException, SecurityException {
		log.info("Setting up REST client:");
		this.proxy = proxyHost;
		// Authentication
		log.info("  Using Basic Authentiction for user " + username);
		HttpHost target = getHttpHost(uri);
		log.debug("  Using credentials provider " + BasicCredentialsProvider.class);
		log.debug("  Authentication scope is " + target);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
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
		HttpClientBuilder clientBuilder = HttpClients.custom().setDefaultCredentialsProvider(credsProvider);
		// Proxy
		if (this.proxy != null) {
			log.info("  Using proxy: " + this.proxy);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(this.proxy);
			clientBuilder.setRoutePlanner(routePlanner);
		} else {
			log.info("  No proxy specified");
		}
		this.httpclient = clientBuilder.build();
	}

	/**
	 * Closes the Confluence client and associated resources, like the HTTP client
	 * and the {@link ExecutorService}.
	 */
	public void close() {
		log.debug("Closing HTTP client");
		try {
			this.httpclient.close();
		} catch (IOException ex) {
			log.error("Error closing HTTP client", ex);
		}
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

	private JsonReader getJsonReader(CloseableHttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		return toJsonReader(inputStream);
	}

	@Override
	public <T> T executeGetRequest(URI uri, Class<T> resultClass) throws RequestException {
		log.debug("Executing request " + uri);
		HttpGet httpRequest = HttpMethodFactory.createGetMethod(uri);
		try {
			return executeRequest(httpRequest, resultClass);
		} catch (IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	private <T> T executeRequest(HttpRequestBase httpRequest, Class<T> resultClass)
			throws ClientProtocolException, IOException, RestException {
		CloseableHttpResponse response = this.httpclient.execute(httpRequest, this.clientContext);
		int statusCode = response.getStatusLine().getStatusCode();
		log.debug("Received status code " + statusCode + " from " + httpRequest);
		switch (statusCode) {
		case HttpURLConnection.HTTP_OK:
			log.debug("Tranforming result into " + resultClass);
			JsonReader jsonReader = getJsonReader(response);
			T result = getGson().fromJson(jsonReader, resultClass);
			httpRequest.releaseConnection();
			return result;
		case HttpURLConnection.HTTP_UNAUTHORIZED:
		case HttpURLConnection.HTTP_FORBIDDEN:
			throw createRestException(response, SecurityException.class);
		default:
			throw createRestException(response, RestException.class);
		}
	}

	@Override
	public InputStream executeGetRequestForDownload(URI uri) throws RequestException {
		log.debug("Executing request " + uri);
		HttpGet method = HttpMethodFactory.createGetMethodForDownload(uri);
		try {
			return executeRequest(method);
		} catch (IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	private InputStream executeRequest(HttpGet httpRequest) throws IOException, RestException {
		log.debug("Executing request " + httpRequest);
		CloseableHttpResponse response = this.httpclient.execute(httpRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		log.debug("Received status code " + statusCode + " from " + httpRequest);
		switch (statusCode) {
		case HttpURLConnection.HTTP_OK:
			log.debug("Transforming result into " + InputStream.class);
			return response.getEntity().getContent();
		case HttpURLConnection.HTTP_UNAUTHORIZED:
		case HttpURLConnection.HTTP_FORBIDDEN:
			throw createRestException(response, SecurityException.class);
		default:
			throw createRestException(response, RestException.class);
		}
	}

	@Override
	public <T> T executePostRequest(URI uri, Object content, Class<T> resultClass) throws RequestException {
		try {
			String body = getGson().toJson(content);
			HttpPost method = HttpMethodFactory.createPostMethod(uri, body);
			return executeRequest(method, resultClass);
		} catch (IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	@Override
	public <T> T executePostRequestForUpload(URI uri, InputStream inputStream, String title, String comment,
			Class<T> resultClass) throws RequestException {
		try {
			HttpPost method = HttpMethodFactory.createPostMethodForUpload(uri, inputStream, title, comment);
			return executeRequest(method, resultClass);
		} catch (IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	private RestException createRestException(HttpResponse response, Class<? extends RestException> exclass) {
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		String reasonPhrase = statusLine.getReasonPhrase();
		String responseBody = null;
		try {
			if (response.getEntity() != null && response.getEntity().getContent() != null) {
				responseBody = IOUtils.toString(response.getEntity().getContent());
			}
		} catch (IOException ioe) {
			log.warn(("Error reading response " + response));
		}
		boolean hasBody = (responseBody != null);
		String message = "Status: " + statusCode + ". Reason: " + reasonPhrase + ". Has body: " + hasBody;
		if (exclass.equals(RestException.class))
			return new RestException(statusCode, reasonPhrase, responseBody, message, null);
		if (exclass.equals(SecurityException.class))
			return new SecurityException(statusCode, reasonPhrase, message, null);
		throw new IllegalArgumentException("Unsupported exception class " + exclass);
	}

}
