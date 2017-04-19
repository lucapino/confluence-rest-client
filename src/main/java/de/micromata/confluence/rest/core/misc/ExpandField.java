package de.micromata.confluence.rest.core.misc;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 04.07.2016
 * Mdodified: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public enum ExpandField {
    SPACE("space"),
    BODY_VIEW("body.view"),
    BODY_STORAGE("body.storage"),
    VERSION("version"),
    CONTAINER("container"),
    HISTORY("history"),
    CHILDREN("children"),
    OPERATIONS("operations"),
    DESCENDANTS("descendants"),
    ANCESTORS("ancestors"),
    RESTRICTIONS("restrictions");

    String name;

    ExpandField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
