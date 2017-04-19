package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import de.micromata.confluence.rest.core.domain.common.ExpandableBean;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 04.07.2016
 * Modified: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public class StorageBean {

    @Expose
    private String value;

    @Expose
    private String representation;

    @Expose
    private ExpandableBean expandable;

    public ExpandableBean getExpandable() {
        return expandable;
    }

    public void setExpandable(ExpandableBean expandable) {
        this.expandable = expandable;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
