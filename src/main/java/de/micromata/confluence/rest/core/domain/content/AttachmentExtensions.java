package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;

/**
 * Author: Martin Böhmer (kontakt@itboehmer.de)
 * Created: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public class AttachmentExtensions {

    @Expose
    private String comment;

    @Expose
    private long fileSize;

    @Expose
    private String mediaType;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

}
