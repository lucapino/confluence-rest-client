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
package de.itboehmer.confluence.rest.core.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public class HttpMethodFactory {
    
    public static HttpGet createGetMethod(URI uri) {
        HttpGet method = new HttpGet(uri);
        setHeadersForJsonExchange(method);
        return method;
    }
    
    public static HttpGet createGetMethodForDownload(URI uri) {
        HttpGet method = new HttpGet(uri);
        method.addHeader(HttpHeaders.ACCEPT,
                MediaType.APPLICATION_OCTET_STREAM);
        return method;
    }
    
    public static HttpPost createPostMethod(URI uri, String body) throws UnsupportedEncodingException {
        if (uri == null) {
            return null;
        }
        HttpPost method = new HttpPost(uri);
        setHeadersForJsonExchange(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }
    
    public static HttpPut createPutMethod(URI uri, String body) throws UnsupportedEncodingException {
        if (uri == null) {
            return null;
        }
        HttpPut method = new HttpPut(uri);
        setHeadersForJsonExchange(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }
    
    public static HttpPost createPostMethodForUpload(URI uri, InputStream inputStream, String filename, String comment) throws FileNotFoundException {
        HttpPost method = new HttpPost(uri);
        method.addHeader("X-Atlassian-Token", "no-check");
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addBinaryBody("file", inputStream, org.apache.http.entity.ContentType.DEFAULT_BINARY, filename);
        if (comment != null) {
            entityBuilder.addTextBody("comment", comment);
        }
        method.setEntity(entityBuilder.build());
        return method;
    }
    
    private static void setHeadersForJsonExchange(HttpMessage httpMessage) {
        httpMessage.addHeader(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
        httpMessage.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }
}
