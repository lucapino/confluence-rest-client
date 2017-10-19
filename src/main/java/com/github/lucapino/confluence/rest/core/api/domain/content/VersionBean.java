/**
 * Copyright 2016 Micromata GmbH
 * Modifications Copyright 2017 Luca Tagliani
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
import com.github.lucapino.confluence.rest.core.api.domain.UserBean;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 */
public class VersionBean {

    @Expose
    private UserBean by;
    @Expose
    private String when;
    @Expose
    private String message;
    @Expose
    private Integer number;
    @Expose
    private Boolean minorEdit;

    public UserBean getBy() {
        return by;
    }

    public void setBy(UserBean by) {
        this.by = by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getMinorEdit() {
        return minorEdit;
    }

    public void setMinorEdit(Boolean minorEdit) {
        this.minorEdit = minorEdit;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}
