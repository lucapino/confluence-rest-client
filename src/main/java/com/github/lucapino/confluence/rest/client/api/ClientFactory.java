/* 
 * Copyright 2016 Micromata GmbH
 * Modifications Copyright 2017 Martin Böhmer
 * Modifications Copyright 2017 Luca Tagliani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lucapino.confluence.rest.client.api;

/**
 * ClientFactory is the root of services for interacting with a Confluence
 * installation.
 *
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 * @author Luca Tagliani
 */
public interface ClientFactory {

    /**
     * Returns a {@link UserClient} for managing user, groups and memberships.
     *
     * @return the {@link UserClient}
     */
    UserClient getUserClient();

    /**
     * Returns a {@link SpaceClient} for managing spaces.
     *
     * @return the {@link SpaceClient}
     */
    SpaceClient getSpaceClient();

    /**
     * Returns a {@link ContentClient} for managing contents.
     *
     * @return the {@link ContentClient}
     */
    ContentClient getContentClient();

    /**
     * Returns a {@link SearchClient} for searching.
     *
     * @return the {@link SearchClient}
     */
    SearchClient getSearchClient();
}
