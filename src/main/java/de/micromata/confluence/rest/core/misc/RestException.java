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
package de.micromata.confluence.rest.core.misc;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import org.apache.commons.io.IOUtils;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class RestException extends Exception {

    private static final long serialVersionUID = -6402297688207704131L;

    private final int statusCode;

    private final String reasonPhrase;

    private String responseBody;

    private final String message;

    protected RestException(int statusCode, String reasonphrase, String responseBody, String message) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonphrase;
        this.responseBody = responseBody;
        this.message = message;
    }

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
        boolean hasBody = (this.responseBody != null);
        this.message = "Status: " + this.statusCode + ". Reason: " + this.reasonPhrase + ". Has body: " + hasBody;
    }

    @Override
    public String getMessage() {
        return this.message;
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
