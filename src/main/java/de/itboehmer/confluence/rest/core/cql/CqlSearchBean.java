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
package de.itboehmer.confluence.rest.core.cql;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 04.07.2016
 * Project: ConfluenceTransferPlugin
 */
public class CqlSearchBean {

    private String cql;

    private String cqlcontext;

    private ExcerptStrategy excerpt;

    private List<String> expand = new ArrayList<>();

    private int start;

    private int limit;

    private boolean includeArchivedSpaces;

    public String getCql() {
        return cql;
    }

    public void setCql(String cql) {
        this.cql = cql;
    }

    public String getCqlcontext() {
        return cqlcontext;
    }

    public void setCqlcontext(String cqlcontext) {
        this.cqlcontext = cqlcontext;
    }

    public ExcerptStrategy getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(ExcerptStrategy excerpt) {
        this.excerpt = excerpt;
    }

    public List<String> getExpand() {
        return expand;
    }

    public void setExpand(List<String> expand) {
        this.expand = expand;
    }

    public boolean isIncludeArchivedSpaces() {
        return includeArchivedSpaces;
    }

    public void setIncludeArchivedSpaces(boolean includeArchivedSpaces) {
        this.includeArchivedSpaces = includeArchivedSpaces;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
