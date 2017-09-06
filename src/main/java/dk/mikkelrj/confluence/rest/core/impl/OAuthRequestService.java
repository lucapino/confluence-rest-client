package dk.mikkelrj.confluence.rest.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.MultipartContent.Part;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import de.itboehmer.confluence.rest.core.misc.RestException;
import de.itboehmer.confluence.rest.core.misc.SecurityException;
import dk.mikkelrj.confluence.rest.core.RequestException;
import dk.mikkelrj.confluence.rest.core.RequestService;
import dk.mikkelrj.confluence.rest.oauth.JiraOAuthClient;
import dk.mikkelrj.confluence.rest.oauth.OAuthProperties;

/**
 * {@link RequestService} implementation that authenticates users through the
 * OAuth protocol.
 */
public class OAuthRequestService extends AbstractRequestService implements RequestService {

	private static final Logger log = LoggerFactory.getLogger(HttpAuthRequestService.class);

	private JiraOAuthClient jiraOAuthClient;

	private OAuthProperties authProps;

	public OAuthRequestService(OAuthProperties authProps) throws Exception {
		this.authProps = authProps;
		this.jiraOAuthClient = new JiraOAuthClient(authProps);
	}

	@Override
	public <T> T executeGetRequest(URI uri, Class<T> resultClass) throws RequestException {
		try {
			HttpResponse response = getResponseFromUrl(getOAuthParameters(), new GenericUrl(uri));
			return executeRequest(uri, response, resultClass);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	private <T> T executeRequest(URI uri, HttpResponse response, Class<T> resultClass) throws IOException, RestException {
		int statusCode = response.getStatusCode();
		log.debug("Received status code " + statusCode + " from " + uri);
		switch (statusCode) {
		case HttpURLConnection.HTTP_OK:
			log.debug("Tranforming result into " + resultClass);
			JsonReader jsonReader = getJsonReader(response);
			T result = getGson().fromJson(jsonReader, resultClass);
			response.disconnect();
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
		try {
			HttpResponse response = getResponseFromUrl(getOAuthParameters(), new GenericUrl(uri));
			int statusCode = response.getStatusCode();
			log.debug("Received status code " + statusCode + " from " + uri);
			switch (statusCode) {
			case HttpURLConnection.HTTP_OK:
				log.debug("Transforming result into " + InputStream.class);
				return response.getContent();
			case HttpURLConnection.HTTP_UNAUTHORIZED:
			case HttpURLConnection.HTTP_FORBIDDEN:
				throw createRestException(response, SecurityException.class);
			default:
				throw createRestException(response, RestException.class);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | RestException e) {
			throw new RequestException(e);
		}
	}

	@Override
	public <T> T executePostRequest(URI uri, Object content, Class<T> resultClass) throws RequestException {
		try {
			HttpResponse response = getResponseFromPostUrl(getOAuthParameters(), new GenericUrl(uri), new JsonHttpContent(new JacksonFactory(), content));
			JsonReader jsonReader = getJsonReader(response);
			T result = getGson().fromJson(jsonReader, resultClass);
			response.disconnect();
			return result;
		} catch (JsonIOException | JsonSyntaxException | NoSuchAlgorithmException | InvalidKeySpecException
				| IOException e) {
			throw new RequestException(e);
		}
	}

	@Override
    public <T> T executePostRequestForUpload(URI uri, InputStream inputStream, String title,
			String comment, Class<T> resultClass) throws RequestException {
		try {
			HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(getOAuthParameters());
			MultipartContent content = new MultipartContent().setMediaType(
			        new HttpMediaType("multipart/form-data")
			                .setParameter("boundary", "__END_OF_PART__"));
		    MultipartContent.Part commentPart = new Part(ByteArrayContent.fromString(null, comment));
		    commentPart.setHeaders(new HttpHeaders().set("Content-Disposition", "form-data; name=\"comment\""));
		    content.addPart(commentPart);

		    Part filePart = new Part(new InputStreamContent("application/octet-stream", inputStream));
		    filePart.setHeaders(new HttpHeaders().set(
		            "Content-Disposition", 
		            String.format("form-data; name=\"content\"; filename=\"%s\"", title)));
			content.addPart(filePart);
			HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(uri), content);
			request.setHeaders(request.getHeaders().set("X-Atlassian-Token", "no-check"));

			HttpResponse response = getResponseFromPostUrl(getOAuthParameters(), new GenericUrl(uri), new JsonHttpContent(new JacksonFactory(), content));
			JsonReader jsonReader = getJsonReader(response);
			T result = getGson().fromJson(jsonReader, resultClass);
			response.disconnect();
			return result;
		} catch (JsonIOException | JsonSyntaxException | NoSuchAlgorithmException | InvalidKeySpecException
				| IOException e) {
			throw new RequestException(e);
		}
	}

	private RestException createRestException(HttpResponse response, Class<? extends RestException> exclass) {
        int statusCode = response.getStatusCode();
        String reasonPhrase = response.getStatusMessage();
        String responseBody = null;
		try {
            if (response.getContent() != null) {
                responseBody = IOUtils.toString(response.getContent());
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

	protected JsonReader getJsonReader(HttpResponse response) throws IOException {
		return toJsonReader(response.getContent());
	}
	
	/**
	 * Authenticates using the given OAuthParameters and makes request to URL
	 *
	 * @param parameters
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private HttpResponse getResponseFromUrl(OAuthParameters parameters, GenericUrl url) throws IOException {
		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
		HttpRequest request = requestFactory.buildGetRequest(url);
		return request.execute();
	}

	/**
	 * Authenticates using the given OAuthParameters and makes request to URL
	 *
	 * @param parameters
	 * @param url
	 * @param content 
	 * @return
	 * @throws IOException
	 */
	private HttpResponse getResponseFromPostUrl(OAuthParameters parameters, GenericUrl url, HttpContent content) throws IOException {
		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
		HttpRequest request = requestFactory.buildPostRequest(url, content);
		return request.execute();
	}

	private OAuthParameters getOAuthParameters() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String tmpToken = authProps.get(OAuthProperties.ACCESS_TOKEN);
		String secret = authProps.get(OAuthProperties.SECRET);
		String consumerKey = authProps.get(OAuthProperties.CONSUMER_KEY);
		String privateKey = authProps.get(OAuthProperties.PRIVATE_KEY);
		return jiraOAuthClient.getParameters(tmpToken, secret, consumerKey, privateKey);
	}

}
