package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import de.micromata.confluence.rest.core.domain.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 04.07.2016
 * Modified: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public class AncestorBean extends BaseBean {

    @Expose
    private String status;

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
    private BodyBean body;

    @Expose
    private MetadataBean metadata;

    @Expose
    private RestrictionsBean restrictions;

    public AncestorBean() {
    }
    
    public AncestorBean(String id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
