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
package com.github.lucapino.confluence.rest.core.api.domain;

import com.google.gson.annotations.Expose;
import com.github.lucapino.confluence.rest.core.api.domain.common.LinksBean;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 */
public class UserBean {

    @Expose
    private String type;

    @Expose
    private String username;

    @Expose
    private String userKey;

    @Expose
    private ProfilePictureBean profilePicture;

    @Expose
    private String displayName;

    @Expose
    private LinksBean links;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public ProfilePictureBean getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePictureBean profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }
}
