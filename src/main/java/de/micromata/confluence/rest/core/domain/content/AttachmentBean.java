package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import de.micromata.confluence.rest.core.domain.BaseBean;
import de.micromata.confluence.rest.core.domain.space.SpaceBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Author: Martin Böhmer (kontakt@itboehmer.de)
 * Created: 19.04.2017
 * Project: ConfluenceTransferPlugin
 */
public class AttachmentBean extends BaseBean {

    @Expose
    private String title;

    @Expose
    private SpaceBean space;

    @Expose
    private VersionBean version;

    @Expose
    private MetadataBean metadata;

    @Expose
    private AttachmentExtensions extensions;

    private InputStream inputStream;

    public AttachmentBean() {
    }

    public AttachmentBean(String id) {
        super(id);
    }

    public AttachmentBean(File f, String comment) throws FileNotFoundException {
        this(new FileInputStream(f), f.getName(), comment);
    }

    public AttachmentBean(InputStream inputStream, String filename, String comment) {
        this.title = filename;
        this.inputStream = inputStream;
        if (comment != null) {
            this.extensions = new AttachmentExtensions();
            this.extensions.setComment(comment);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SpaceBean getSpace() {
        return space;
    }

    public void setSpace(SpaceBean space) {
        this.space = space;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public AttachmentExtensions getExtensions() {
        return extensions;
    }

    public void setExtensions(AttachmentExtensions extensions) {
        this.extensions = extensions;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

}
