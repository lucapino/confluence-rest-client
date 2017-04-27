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
package de.itboehmer.confluence.rest.core.domain.content;

import de.itboehmer.confluence.rest.client.ContentClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class AttachmentBean extends ContentBean {

    private InputStream inputStream;

    private ContentClient contentClient;

    public AttachmentBean() {
    }

    public AttachmentBean(String id) {
        super(id);
    }

    public AttachmentBean(File f, String comment) throws FileNotFoundException {
        this(new FileInputStream(f), f.getName(), comment);
    }

    public AttachmentBean(InputStream inputStream, String filename, String comment) {
        this.setTitle(filename);
        this.inputStream = inputStream;
        if (comment != null) {
            this.setMetadata(new MetadataBean());
            this.getMetadata().setComment(comment);
        }
    }

    public InputStream getInputStream() {
        if (this.inputStream != null) {
            return inputStream;
        } else if (this.contentClient != null) {
            //Future<InputStream> result = this.contentClient.downloadAttachement(this);
            //return result.get();
            return null;
        } else {
            throw new IllegalStateException("Neither an input stream, nor a content client is set!");
        }
    }

}
