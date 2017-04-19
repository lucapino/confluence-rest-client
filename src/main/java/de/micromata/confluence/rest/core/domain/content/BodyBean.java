package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;

/**
 * Authors: Christian Schulze (c.schulze@micromata.de), Martin Böhmer (mb@itboehmer.de)
 * Created: 04.07.2016
 * Modified: 19.04.2016
 * Project: ConfluenceTransferPlugin
 */
public class BodyBean {

    @Expose
    private ViewBean view;
    
    @Expose
    private StorageBean storage;

    public ViewBean getView() {
        return view;
    }

    public void setView(ViewBean view) {
        this.view = view;
    }

    public StorageBean getStorage() {
        return storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }
}
