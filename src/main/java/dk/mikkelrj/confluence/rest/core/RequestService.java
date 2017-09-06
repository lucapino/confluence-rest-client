package dk.mikkelrj.confluence.rest.core;

import java.io.InputStream;
import java.net.URI;

/**
 * Supports making requests to a REST API.
 */
public interface RequestService {

	<T> T executeGetRequest(URI uri, Class<T> resultClass) throws RequestException;

	InputStream executeGetRequestForDownload(URI uri) throws RequestException;

	<T> T executePostRequest(URI uri, Object content, Class<T> resultClass) throws RequestException;

	<T> T executePostRequestForUpload(URI uri, InputStream inputStream, String title, String comment,
			Class<T> resultClass) throws RequestException;

}
