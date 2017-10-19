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
package com.github.lucapino.confluence.rest.core.api.domain.content;

import com.google.gson.annotations.Expose;

/**
 * @author Martin Böhmer
 */
public class LabelBean {

    @Expose
    private String prefix;

    @Expose
    private String name;

    @Expose
    private String id;

    public LabelBean() {
    }

    public LabelBean(String prefix, String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public LabelBean(String prefix, String name, String id) {
        this.prefix = prefix;
        this.name = name;
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" (prefix=");
        sb.append(prefix);
        if (id != null && !id.isEmpty()) {
            sb.append(", id=");
            sb.append(id);
        }
        sb.append(")");
        return sb.toString();
    }

}
