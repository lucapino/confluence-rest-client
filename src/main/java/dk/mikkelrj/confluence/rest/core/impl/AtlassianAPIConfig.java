package dk.mikkelrj.confluence.rest.core.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.itboehmer.confluence.rest.core.misc.RestPathConstants;

public class AtlassianAPIConfig {

	private static final Logger log = LoggerFactory.getLogger(AtlassianAPIConfig.class);
	
    private URI baseUri;
    private URI restApiBaseUri;

    public AtlassianAPIConfig(URI uri) throws URISyntaxException {
		this.baseUri = Preconditions.checkNotNull(uri);
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
