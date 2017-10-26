/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.confluence.rest.core.api.domain.content;

import com.github.lucapino.confluence.rest.core.api.domain.BaseBean;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceBean;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Tagliani
 */
public class CommentBean extends BaseBean {

    @Expose
    private BodyBean body;

    @Expose
    private ContainerBean container;

    @Expose
    private VersionBean version;

    @Expose
    private List<AncestorBean> ancestors = new ArrayList<>();

    @Expose
    private SpaceBean space;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public ContainerBean getContainer() {
        return container;
    }

    public void setContainer(ContainerBean container) {
        this.container = container;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public List<AncestorBean> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<AncestorBean> ancestors) {
        this.ancestors = ancestors;
    }

    public SpaceBean getSpace() {
        return space;
    }

    public void setSpace(SpaceBean space) {
        this.space = space;
    }

}
