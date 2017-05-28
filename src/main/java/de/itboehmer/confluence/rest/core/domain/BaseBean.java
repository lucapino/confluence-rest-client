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
package de.itboehmer.confluence.rest.core.domain;

import com.google.gson.annotations.Expose;
import de.itboehmer.confluence.rest.core.domain.common.LinksBean;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public abstract class BaseBean {

    @Expose
    private String id;

    @Expose
    private String type;

    @Expose
    private LinksBean _links;

    public BaseBean() {
    }

    public BaseBean(String id) {
        this.id = id;
    }

    public BaseBean(String id, String type, LinksBean links) {
        this.id = id;
        this.type = type;
        this._links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinksBean getLinks() {
        return _links;
    }

    public void setLinks(LinksBean links) {
        this._links = links;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
