package de.micromata.confluence.rest.core.misc;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 01.07.2016
 * Modified: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public class RestException extends Exception {

    private final int statusCode;

    private final String reasonPhrase;

    private String responseBody;

    public RestException(CloseableHttpResponse response) {
        StatusLine statusLine = response.getStatusLine();
        this.statusCode = statusLine.getStatusCode();
        this.reasonPhrase = statusLine.getReasonPhrase();
        try {
            if (response.getEntity() != null && response.getEntity().getContent() != null) {
                this.responseBody = IOUtils.toString(response.getEntity().getContent());
            }
        } catch (IOException ioe) {
            // Noop. Should be logged.
        }
    }

    @Override
    public String toString() {
        boolean hasBody = (this.responseBody != null);
        return "REST exception. Status: " + this.statusCode + ". Reason: " + this.reasonPhrase + ". Has body: " + hasBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public String getResponseBody() {
        return responseBody;
    }

}
