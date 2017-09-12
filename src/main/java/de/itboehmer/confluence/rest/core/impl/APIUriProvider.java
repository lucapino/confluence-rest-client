package de.itboehmer.confluence.rest.core.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.Validate;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.itboehmer.confluence.rest.core.misc.RestPathConstants;

public class APIUriProvider {

	private static final Logger log = LoggerFactory.getLogger(APIUriProvider.class);
	
    private URI baseUri;
    private URI restApiBaseUri;

    public APIUriProvider(URI uri) throws URISyntaxException {
    		Validate.notNull(uri);
		this.baseUri = uri;
        this.restApiBaseUri = buildRestApiBaseURI(uri);
        log.info("  URI: " + this.restApiBaseUri);
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
