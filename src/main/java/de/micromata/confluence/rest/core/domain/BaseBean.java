package de.micromata.confluence.rest.core.domain;

import com.google.gson.annotations.Expose;
import de.micromata.confluence.rest.core.domain.common.LinksBean;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 02.07.2016
 * Modified: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public abstract class BaseBean {

    @Expose
    private String id;

    @Expose
    private String type;

    @Expose
    private LinksBean links;

    public BaseBean() {
    }
    
    public BaseBean(String id) {
        this.id = id;
    }

    public BaseBean(String id, String type, LinksBean links) {
        this.id = id;
        this.type = type;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
