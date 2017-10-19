/**
 * Copyright 2016 Micromata GmbH
 * Modifications Copyright 2017 Martin Böhmer
 * Modifications Copyright 2017 Luca Tagliani
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
package com.github.lucapino.confluence.rest.client.api;

import com.github.lucapino.confluence.rest.core.api.domain.content.AttachmentBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentResultsBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.LabelBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.LabelsBean;
import com.github.lucapino.confluence.rest.core.api.misc.ContentStatus;
import com.github.lucapino.confluence.rest.core.api.misc.ContentType;
import java.io.InputStream;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Client to receive Content from a confluence server.
 *
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public interface ContentClient {

    /**
     * Get Content by ID
     *
     * @param id      The ID of the content
     * @param version The Version of the content
     * @param expand  A List of Fields to expand
     *
     * @return A {@link Future} with the {@link ContentBean}
     */
    Future<ContentBean> getContentById(String id, int version, List<String> expand);

    /**
     * Returns a paginated list of Content.
     *
     * @param type       the content type to return. Default value: page. Valid
     *                   values: page, blogpost.
     * @param spacekey   the space key to find content under.
     * @param title      the title of the page to find. Required for page type.
     * @param status     list of statuses the content to be found is in.
     *                   Defaults to
     *                   current is not specified. If set to 'any', content in 'current' and
     *                   'trashed' status will be fetched. Does not support 'historical' status
     *                   for now.
     * @param postingDay the posting day of the blog post.
     * @param expand     a comma separated list of properties to expand on the
     *                   content. Default value: history,space,version.
     * @param start      the start point of the collection to return
     * @param limit      the limit of the number of items to return, this may be
     *                   restricted by fixed system limits
     *
     * @return A {@link Future} with the {@link ContentResultsBean}
     */
    Future<ContentResultsBean> getContent(ContentType type,
            String spacekey,
            String title,
            ContentStatus status,
            Date postingDay,
            List<String> expand,
            int start,
            int limit);

    /**
     * Creates the provided content.
     *
     * @param content The content to create.
     *
     * @return A {@link Future} with the {@link ContentBean} representing the
     *         created content. Use this instance to continue working with the content.
     */
    public Future<ContentBean> createContent(ContentBean content);

    /**
     * Updates the provided content.
     *
     * @param content The content to update.
     *
     * @return A {@link Future} with the {@link ContentBean} representing the
     *         updated content. Use this instance to continue working with the content.
     */
    public Future<ContentBean> updateContent(ContentBean content);

    /**
     * Uploads the provided attachment to content (page or blogpost).
     *
     * @param attachment    The attachement to upload.
     * @param parentContent The content (page or blogpost) to upload the
     *                      attachment to.
     *
     * @return A {@link Future} with the {@link AttachmentBean} representing the
     *         uploaded attachement. Use this instance to continue working with the
     *         attachment.
     */
    public Future<AttachmentBean> uploadAttachment(AttachmentBean attachment, ContentBean parentContent);

    /**
     * Downalds the given attachment.
     *
     * @param attachment the attachment to downlad, represented by an
     *                   {@link AttachmentBean}
     *
     * @return {@link InputStream} providing the attachment's data.
     */
    public Future<InputStream> downloadAttachement(AttachmentBean attachment);

    /**
     * Adds a set of labels to a given content.
     *
     * @param content the content to add the labels to.
     * @param labels  the label to add, represetned as {@link LabelBean}s
     *
     * @return the updated labels as a {@link LabelsBean}
     */
    public Future<LabelsBean> addLabels(ContentBean content, List<LabelBean> labels);

}
