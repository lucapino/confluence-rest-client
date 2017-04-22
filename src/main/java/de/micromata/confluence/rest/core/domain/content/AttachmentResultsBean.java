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
package de.micromata.confluence.rest.core.domain.content;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class AttachmentResultsBean {

    @Expose
    private List<AttachmentBean> results = new ArrayList<>();

    @Expose
    private Integer start;

    @Expose
    private Integer limit;

    @Expose
    private Integer size;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<AttachmentBean> getResults() {
        return results;
    }

    public void setResults(List<AttachmentBean> results) {
        this.results = results;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

}
