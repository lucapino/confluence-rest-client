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
package de.micromata.confluence.rest.core;

import com.google.gson.stream.JsonReader;
import de.micromata.confluence.rest.ConfluenceRestClient;
import de.micromata.confluence.rest.client.ContentClient;
import de.micromata.confluence.rest.core.domain.content.AttachmentBean;
import de.micromata.confluence.rest.core.domain.content.AttachmentResultsBean;
import de.micromata.confluence.rest.core.domain.content.ContentBean;
import de.micromata.confluence.rest.core.domain.content.ContentResultsBean;
import de.micromata.confluence.rest.core.misc.AuthorizationException;
import de.micromata.confluence.rest.core.misc.ContentStatus;
import de.micromata.confluence.rest.core.misc.ContentType;
import de.micromata.confluence.rest.core.misc.RestException;
import de.micromata.confluence.rest.core.misc.UnexpectedContentException;
import de.micromata.confluence.rest.core.util.HttpMethodFactory;
import de.micromata.confluence.rest.core.util.URIHelper;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.HttpURLConnection;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class ContentClientImpl extends BaseClient implements ContentClient {

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ContentClientImpl(ConfluenceRestClient confluenceRestClient, ExecutorService executorService) {
        super(confluenceRestClient, executorService);
    }

    @Override
    public Future<ContentBean> getContentById(String id, int version, List<String> expand) {
        return executorService.submit(() -> {
            // URI with parameters
            URIBuilder uriBuilder = URIHelper.buildPath(baseUri, CONTENT, id);
            if (version > 0) {
                uriBuilder.addParameter(VERSION, String.valueOf(version));
            }
            if (CollectionUtils.isNotEmpty(expand) == true) {
                String join = StringUtils.join(expand, ",");
                uriBuilder.addParameter(EXPAND, join);
            }
            HttpGet method = HttpMethodFactory.createGetMethod(uriBuilder.build());
            return executeRequest(method, ContentBean.class);
        });
    }

    @Override
    public Future<ContentResultsBean> getContent(ContentType type, String spacekey, String title, ContentStatus status, Date postingDay, List<String> expand, int start, int limit) {
        // URI with parameters
        URIBuilder uriBuilder = buildPath(CONTENT);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (type != null) {
            nameValuePairs.add(new BasicNameValuePair(TYPE, type.getName()));
        }
        if (StringUtils.trimToNull(spacekey) != null) {
            nameValuePairs.add(new BasicNameValuePair(SPACEKEY, spacekey));
        }
        if (StringUtils.trimToNull(title) != null) {
            nameValuePairs.add(new BasicNameValuePair(TITLE, title));
        }
        if (status != null) {
            nameValuePairs.add(new BasicNameValuePair(STATUS, status.getName()));
        }
        if (postingDay != null) {
            nameValuePairs.add(new BasicNameValuePair(POSTING_DAY, sdf.format(postingDay)));
        }
        if (expand != null && expand.isEmpty() == false) {
            String join = StringUtils.join(expand, ",");
            nameValuePairs.add(new BasicNameValuePair(EXPAND, join));
        }
        if (start > 0) {
            nameValuePairs.add(new BasicNameValuePair(START, String.valueOf(start)));
        }
        if (limit > 0) {
            nameValuePairs.add(new BasicNameValuePair(LIMIT, String.valueOf(limit)));
        }
        uriBuilder.addParameters(nameValuePairs);
        // Request
        return executorService.submit(() -> {
            HttpGet method = HttpMethodFactory.createGetMethod(uriBuilder.build());
            return executeRequest(method, ContentResultsBean.class);
        });
    }

    public Future<ContentBean> createContent(ContentBean content) {
        // Encode content
        String body = gson.toJson(content);
        // Request
        return executorService.submit(() -> {
            URI uri = buildPath(CONTENT).build();
            HttpPost method = HttpMethodFactory.createPostMethod(uri, body);
            return executeRequest(method, ContentBean.class);
        });
    }

    @Override
    public Future<AttachmentBean> uploadAttachment(AttachmentBean attachment, ContentBean parentContent) {
        return executorService.submit(() -> {
            // URI
            String attachmentUriPath = String.format(CONTENT_ATTACHMENT, parentContent.getId());
            URI uri = buildPath(attachmentUriPath).build();
            // Check for comment
            String comment = null;
            if (attachment.getMetadata() != null && attachment.getMetadata().getComment() != null) {
                comment = attachment.getMetadata().getComment();
            }
            // Request
            HttpPost method = HttpMethodFactory.createPostMethodForUpload(uri, attachment.getInputStream(), attachment.getTitle(), comment);
            AttachmentResultsBean results = executeRequest(method, AttachmentResultsBean.class);
            // Extract result
            int numberOfResults = 0;
            if (results.getResults() != null) {
                numberOfResults = results.getResults().size();
            }
            if (numberOfResults == 1) {
                return (AttachmentBean) results.getResults().get(0);
            } else {
                throw new UnexpectedContentException("Attachment result set with 1 element", "Attachemnt result set with " + numberOfResults + " elements");
            }
        });
    }

    public Future<InputStream> downloadAttachement(AttachmentBean attachment) {
        // Check input
        if (attachment.getId() == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (attachment.getTitle() == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        return executorService.submit(() -> {
            // URI
            String downloadUriPath = String.format(DOWNLOAD_ATTACHMENT, attachment.getId(), attachment.getTitle());
            URI uri = buildPath(downloadUriPath).build();
            // Request
            HttpGet method = HttpMethodFactory.createGetMethodForDownload(uri);
            CloseableHttpResponse response = client.execute(method, clientContext);
            return executeRequest(method);
        });
    }

    private <T> T executeRequest(HttpRequestBase httpRequest, Class<T> resultClass) throws IOException, AuthorizationException, RestException {
        CloseableHttpResponse response = this.client.execute(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                JsonReader jsonReader = getJsonReader(response);
                T result = gson.fromJson(jsonReader, resultClass);
                httpRequest.releaseConnection();
                return result;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new AuthorizationException(response);
            default:
                throw new RestException(response);
        }
    }

    private InputStream executeRequest(HttpRequestBase httpRequest) throws IOException, AuthorizationException, RestException {
        CloseableHttpResponse response = this.client.execute(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case HttpURLConnection.HTTP_OK:
                return response.getEntity().getContent();
            case HttpURLConnection.HTTP_UNAUTHORIZED:
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new AuthorizationException(response);
            default:
                throw new RestException(response);
        }
    }

}
