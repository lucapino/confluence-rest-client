/**
 * Copyright 2016 Micromata GmbH
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
package de.itboehmer.confluence.rest.core.custom;

import com.google.gson.*;
import de.itboehmer.confluence.rest.core.domain.BaseBean;
import de.itboehmer.confluence.rest.core.domain.content.ContentBean;
import de.itboehmer.confluence.rest.core.domain.cql.CqlSearchResult;
import de.itboehmer.confluence.rest.core.domain.space.SpaceBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 07.07.2016
 * Project: ConfluenceTransferPlugin
 */
public class CqlSearchResultDeserializer extends BaseDeserializer implements JsonDeserializer<CqlSearchResult> {

    private static final String SPACE = "space";
    private static final String CONTENT = "content";

    @Override
    public CqlSearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CqlSearchResult cqlSearchResult = gson.fromJson(json, CqlSearchResult.class);
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement resultsElement = jsonObject.get("results");
        JsonArray resultsArray = resultsElement.getAsJsonArray();
        List<BaseBean> baseBeen = deserializeResultsArray(resultsArray);
        cqlSearchResult.setResults(baseBeen);
        return cqlSearchResult;
    }

    private List<BaseBean> deserializeResultsArray(JsonArray results) {
        List<BaseBean> retval = new ArrayList<>();
        for (JsonElement result : results) {
            JsonObject jsonObject = result.getAsJsonObject();
            if (jsonObject.has("content") == true) {
                JsonElement content = jsonObject.get(CONTENT);
                ContentBean contentBean = gson.fromJson(content, ContentBean.class);
                retval.add(contentBean);
            } else if (jsonObject.has("space") == true) {
                JsonElement space = jsonObject.get(SPACE);
                SpaceBean spaceBean = gson.fromJson(space, SpaceBean.class);
                retval.add(spaceBean);
            } else {
                // nothing
            }
        }
        return retval;
    }

}
