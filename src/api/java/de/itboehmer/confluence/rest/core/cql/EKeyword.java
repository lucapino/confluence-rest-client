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

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 06.07.2016
 * Project: ConfluenceTransferPlugin
 */
public enum EKeyword {

    /**
     * Used to combine multiple clauses, allowing you to refine your search.
     * Note: you can use parentheses to control the order in which clauses are executed.
     */
    AND("and"),

    /**
     * Used to combine multiple clauses, allowing you to expand your search.
     * Note: you can use parentheses to control the order in which clauses are executed.
     * (Note: also see IN, which can be a more convenient way to search for multiple values of a field.)
     */
    OR("or"),

    /**
     * Used to negate individual clauses or a complex CQL query (a query made up of more than one clause) using parentheses,
     * allowing you to refine your search.
     * (Note: also see NOT EQUALS ("!="), DOES NOT CONTAIN ("!~") and NOT IN.)
     */
    NOT("not"),

    /**
     * Used to specify the fields by whose values the search results will be sorted.
     * By default, the field's own sorting order will be used. You can override this by specifying ascending order ("asc") or descending order ("desc").
     * Not all fields support Ordering. Generally, ordering is not supported where a piece of content can have multiple values for a field,
     * for instance ordering is not supported on labels.
     */
    ORDER_BY("order by");
    /**
     * The keyword.
     */
    private final String keyword;

    /**
     * Instantiates a new keyword.
     *
     * @param keyword name of the keyword
     */
    EKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets the keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return getKeyword();
    }
}
