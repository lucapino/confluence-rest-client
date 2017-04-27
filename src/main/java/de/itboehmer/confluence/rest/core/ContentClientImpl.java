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
package de.itboehmer.confluence.rest.core;

import de.itboehmer.confluence.rest.ConfluenceRestClient;
import de.itboehmer.confluence.rest.client.ContentClient;
import de.itboehmer.confluence.rest.core.domain.content.AttachmentBean;
import de.itboehmer.confluence.rest.core.domain.content.AttachmentResultsBean;
import de.itboehmer.confluence.rest.core.domain.content.ContentBean;
import de.itboehmer.confluence.rest.core.domain.content.ContentResultsBean;
import de.itboehmer.confluence.rest.core.misc.ContentStatus;
import de.itboehmer.confluence.rest.core.misc.ContentType;
import de.itboehmer.confluence.rest.core.misc.UnexpectedContentException;
import de.itboehmer.confluence.rest.core.util.HttpMethodFactory;
import de.itboehmer.confluence.rest.core.util.URIHelper;
import java.io.InputStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class ContentClientImpl extends BaseClient implements ContentClient {

    private final Logger log = LoggerFactory.getLogger(ContentClientImpl.class);

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ContentClientImpl(ConfluenceRestClient confluenceRestClient, ExecutorService executorService) {
        super(confluenceRestClient, executorService);
    }

    @Override
    public Future<ContentBean> getContentById(String id, int version, List<String> expand) {
        if (log.isInfoEnabled()) {
            String message = "Getting content by ID. ID=%1$s, version=%2$s, expand=%3$s";
            log.info(String.format(message, id, version, expand));
        }
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
        if (log.isInfoEnabled()) {
            String message = "Getting content. Type=%1$s, space=%2$s, title=%3$s, status=%4$s, postingDay=%5$s, expand=%6$s, start=%7$s, limit=%8$s";
            log.info(String.format(message, type, spacekey, title, status, postingDay, expand, start, limit));
        }
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
        if (log.isInfoEnabled()) {
            String message = "Creating content. Title=%1$s, space=%2$s";
            String spaceKey = (content.getSpace() != null) ? content.getSpace().getKey() : null;
            log.info(String.format(message, content.getTitle(), spaceKey));
        }
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

    @Override
    public Future<InputStream> downloadAttachement(AttachmentBean attachment) {
        // Check input
        if (attachment.getId() == null) {
            throw new IllegalArgumentException("ID of the attachment cannot be null");
        }
        if (attachment.getTitle() == null) {
            throw new IllegalArgumentException("Title of the attachment cannot be null");
        }
        return executorService.submit(() -> {
            // Determine download URI
            String downloadUriPath = null;
            if (attachment.getLinks() != null) {
                // Provided
                downloadUriPath = attachment.getLinks().getDownload();
            } else {
                // Not Provided
                Future<ContentBean> future = getContentById(attachment.getId(), 0, null);
                ContentBean attachmentContent = future.get();
                downloadUriPath = attachmentContent.getLinks().getDownload();
            }
            URI uri = buildNonRestPath(downloadUriPath).build();
            // Request
            HttpGet method = HttpMethodFactory.createGetMethodForDownload(uri);
            return executeRequest(method);
        });
    }

}
