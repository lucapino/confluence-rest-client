/**
 * Copyright 2017 Martin Böhmer
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
package com.github.lucapino.confluence.rest.client.impl;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.core.api.domain.content.AncestorBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.BodyBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.CommentBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.CommentResultsBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContainerBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentResultsBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.StorageBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.VersionBean;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceBean;
import com.github.lucapino.confluence.rest.core.api.misc.ContentStatus;
import com.github.lucapino.confluence.rest.core.api.misc.ContentType;
import com.github.lucapino.confluence.rest.core.api.misc.ExpandField;
import com.github.lucapino.confluence.rest.core.api.util.*;
import com.github.lucapino.confluence.rest.core.impl.APIAuthConfig;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;
import com.github.lucapino.confluence.rest.core.impl.HttpAuthRequestService;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

/**
 * Tests {@link StorageFormatHelper}.
 *
 * @author Martin Böhmer
 */
public class AddCommentTest {

    @Test(enabled = true)
    public void createCommentTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        APIAuthConfig conf = new APIAuthConfig("https://ecm.hitachi-systems-cbt.com", "luca.tagliani", "nzsiJboRa2");
        HttpAuthRequestService requestService = new HttpAuthRequestService();
        requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

        APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, uriProvider);

        ContentResultsBean contentResultsBean = factory.getContentClient().getContent(ContentType.PAGE, "~luca.tagliani", "Test confluence-maven-plugin", ContentStatus.ANY, null, null, 0, 0).get();
        String parentId = contentResultsBean.getResults().get(0).getId();

        // now add another comment under this one
        CommentBean commentBean = createComment(factory, parentId, null, "<p>A new comment test</p>");
        System.out.println(commentBean.getBody().getStorage().getRepresentation());
        System.out.println(commentBean.getBody().getStorage().getValue());

        CommentBean newCommentBean = createComment(factory, parentId, commentBean.getId(), "<p>A second new comment test</p>");
        System.out.println(newCommentBean.getBody().getStorage().getRepresentation());
        System.out.println(newCommentBean.getBody().getStorage().getValue());

        VersionBean versionBean = new VersionBean();
        versionBean.setNumber(newCommentBean.getVersion().getNumber() + 1);
        newCommentBean.setVersion(versionBean);
        newCommentBean.getBody().getStorage().setValue("<p>A modified comment test</p>");

        CommentBean newModifiedCommentBean = factory.getContentClient().updateComment(newCommentBean).get();
        System.out.println(newModifiedCommentBean.getBody().getStorage().getRepresentation());
        System.out.println(newModifiedCommentBean.getBody().getStorage().getValue());

    }

    private CommentBean createComment(ClientFactory factory, String parentId, String ancestorId, String value) throws Exception {
        CommentBean commentBean = new CommentBean();
        SpaceBean spaceBean = new SpaceBean("~luca.tagliani");
        commentBean.setSpace(spaceBean);
        ContainerBean containerBean = new ContainerBean();
        containerBean.setId(parentId);
        containerBean.setType(ContentType.PAGE.getName());
        commentBean.setContainer(containerBean);
        if (StringUtils.isNotBlank(ancestorId)) {
            // add ancestors
            List<AncestorBean> ancestors = new ArrayList<>();
            AncestorBean ancestor = new AncestorBean();
            ancestor.setType(ContentType.COMMENT.getName());
            ancestor.setId(ancestorId);
            ancestors.add(ancestor);
            commentBean.setAncestors(ancestors);
        }
        commentBean.setType(ContentType.COMMENT.getName());
        BodyBean body = new BodyBean();
        StorageBean storage = new StorageBean();
        storage.setRepresentation("storage");
        storage.setValue(value);
        body.setStorage(storage);
        commentBean.setBody(body);
        return factory.getContentClient().createComment(commentBean).get();
    }

    @Test
    public void createBlogTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        APIAuthConfig conf = new APIAuthConfig("https://ecm.hitachi-systems-cbt.com", "luca.tagliani", "nzsiJboRa2");
        HttpAuthRequestService requestService = new HttpAuthRequestService();
        requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

        APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, uriProvider);
        ContentBean contentBean = new ContentBean();
        SpaceBean spaceBean = new SpaceBean("~luca.tagliani");
        contentBean.setType(ContentType.BLOGPOST.getName());
        contentBean.setSpace(spaceBean);
        contentBean.setTitle("New test blog");
        BodyBean body = new BodyBean();
        StorageBean storageBean = new StorageBean();
        storageBean.setValue("<p>Test blog</p>");
        storageBean.setRepresentation("storage");
        body.setStorage(storageBean);
        contentBean.setBody(body);
        factory.getContentClient().createContent(contentBean);
    }

    @Test
    public void getCommentTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        APIAuthConfig conf = new APIAuthConfig("https://ecm.hitachi-systems-cbt.com", "luca.tagliani", "nzsiJboRa2");
        HttpAuthRequestService requestService = new HttpAuthRequestService();
        requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

        APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, uriProvider);

        List<String> expand = Arrays.asList(ExpandField.BODY_VIEW.getName(), ExpandField.BODY_STORAGE.getName());
        CommentResultsBean commentResultsBean = factory.getContentClient().getComment("~luca.tagliani", "Test confluence-maven-plugin", ContentStatus.ANY, null, expand, 0, 0).get();

        for (CommentBean commentBean : commentResultsBean.getResults()) {
            System.out.println(commentBean.getBody().getStorage().getRepresentation());
            System.out.println(commentBean.getBody().getStorage().getValue());
        }

    }

    @Test
    public void convertWikiMarkupTest() throws Exception {

        APIAuthConfig conf = new APIAuthConfig("https://ecm.hitachi-systems-cbt.com", "luca.tagliani", "nzsiJboRa2");
        HttpAuthRequestService requestService = new HttpAuthRequestService();
        requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

        URI uri = new URI(conf.getBaseUrl() + "/confluence/rest/api/contentbody/convert/storage");
        Map<String, String> params = new HashMap<>();
        params.put("value", "{cheese}");
        params.put("representation", "wiki");
//        JSONObject content = new JSONObject(params);
        Map executePostRequest = requestService.executePostRequest(uri, params, Map.class);
        System.out.println(executePostRequest.get("value"));
    }

    @Test
    public void createPageTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        APIAuthConfig conf = new APIAuthConfig("https://ecm.hitachi-systems-cbt.com", "luca.tagliani", "nzsiJboRa2");
        HttpAuthRequestService requestService = new HttpAuthRequestService();
        requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

        APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl() + "/confluence"));
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, uriProvider);

        ContentResultsBean contentResultsBean = factory.getContentClient().getContent(ContentType.PAGE, "~luca.tagliani", "Test confluence-maven-plugin", ContentStatus.ANY, null, null, 0, 0).get();

        String parentId = contentResultsBean.getResults().get(0).getId();
        // create content
        ContentBean contentBean = new ContentBean();
        SpaceBean spaceBean = new SpaceBean("~luca.tagliani");
        contentBean.setSpace(spaceBean);
        List<AncestorBean> ancestors = new ArrayList<>();
        AncestorBean ancestor = new AncestorBean(parentId);
        ancestors.add(ancestor);
        contentBean.setAncestors(ancestors);
        contentBean.setType(ContentType.PAGE.getName());
        contentBean.setSpace(spaceBean);
        contentBean.setTitle("Test page");
        BodyBean body = new BodyBean();
        StorageBean storageBean = new StorageBean();
        storageBean.setValue("<p><strong>Test di pagina</strong></p>");
        storageBean.setRepresentation("storage");
        body.setStorage(storageBean);
        contentBean.setBody(body);
        ContentBean newContentBean = factory.getContentClient().createContent(contentBean).get();

        InputStream in = requestService.executeGetRequestForDownload(new URI(conf.getBaseUrl() + "/confluence" + Format.PDF.url + "?pageId=" + newContentBean.getId()));
        FileOutputStream out = new FileOutputStream("target/test.pdf");
        IOUtils.copy(in, out);
    }

    static enum Format {

        PDF("/spaces/flyingpdf/pdfpageexport.action"), DOC("/exportword");
        private String url;

        private Format(String url) {
            this.url = url;
        }
    }
}
