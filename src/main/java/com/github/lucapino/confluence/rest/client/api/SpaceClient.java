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
package com.github.lucapino.confluence.rest.client.api;

import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceResultsBean;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceBean;
import com.github.lucapino.confluence.rest.core.api.misc.SpaceStatus;
import com.github.lucapino.confluence.rest.core.api.misc.SpaceType;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Client to recieve spaces from a confluence server.
 *
 * @author Christian Schulze (c.schulze@micromata.de)
 */
public interface SpaceClient {

    /**
     * Get a ResultBean with spaces filtered by the given arguments Every
     * parameter can be NULL, then you get all spaces
     *
     * @param keys   a list of space keys
     * @param type   filter the list of spaces returned by type (global,
     *               personal)
     * @param status filter the list of spaces returned by status (current,
     *               archived)
     * @param label  filter the list of spaces returned by label
     * @param expand a comma separated list of properties to expand on the
     *               spaces
     * @param start  the start point of the collection to return
     * @param limit  the limit of the number of spaces to return, this may be
     *               restricted by fixed system limits
     *
     * @return Future with the ResultBean
     *
     * @throws java.net.URISyntaxException (never thrown due to future)
     */
    Future<SpaceResultsBean> getSpaces(List<String> keys, SpaceType type, SpaceStatus status, List<String> label, List<String> expand, int start, int limit) throws URISyntaxException;

    /**
     * Get a SpaceBean for the given key
     *
     * @param key    the key
     * @param expand a comma separated list of properties to expand on the
     *               spaces
     *
     * @return Future with the SpaceBean
     */
    Future<SpaceBean> getSpaceByKey(String key, List<String> expand);
}
