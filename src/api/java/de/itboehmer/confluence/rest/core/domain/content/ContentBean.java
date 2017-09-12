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
package de.itboehmer.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import de.itboehmer.confluence.rest.core.domain.BaseBean;
import de.itboehmer.confluence.rest.core.domain.space.SpaceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public class ContentBean extends BaseBean {

    @Expose
    private String status;

    @Expose
    private String title;

    @Expose
    private SpaceBean space;

    @Expose
    private HistoryBean history;

    @Expose
    private VersionBean version;

    @Expose
    private List<AncestorBean> ancestors = new ArrayList<>();

    @Expose
    private List<OperationBean> operations = new ArrayList<>();

    @Expose
    private ChildrenBean children;

    @Expose
    private ChildTypesBean childTypes;

    @Expose
    private DescendantsBean descendants;

    @Expose
    private ContainerBean container;

    @Expose
    private BodyBean body;

    @Expose
    private MetadataBean metadata;

    @Expose
    private RestrictionsBean restrictions;

    public ContentBean() {
    }

    public ContentBean(String id) {
        super(id);
    }

    public List<AncestorBean> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<AncestorBean> ancestors) {
        this.ancestors = ancestors;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public ChildrenBean getChildren() {
        return children;
    }

    public void setChildren(ChildrenBean children) {
        this.children = children;
    }

    public ChildTypesBean getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(ChildTypesBean childTypes) {
        this.childTypes = childTypes;
    }

    public ContainerBean getContainer() {
        return container;
    }

    public void setContainer(ContainerBean container) {
        this.container = container;
    }

    public DescendantsBean getDescendants() {
        return descendants;
    }

    public void setDescendants(DescendantsBean descendants) {
        this.descendants = descendants;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public List<OperationBean> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationBean> operations) {
        this.operations = operations;
    }

    public RestrictionsBean getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(RestrictionsBean restrictions) {
        this.restrictions = restrictions;
    }

    public SpaceBean getSpace() {
        return space;
    }

    public void setSpace(SpaceBean space) {
        this.space = space;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public HistoryBean getHistory() {
        return history;
    }

    public void setHistory(HistoryBean history) {
        this.history = history;
    }

}
