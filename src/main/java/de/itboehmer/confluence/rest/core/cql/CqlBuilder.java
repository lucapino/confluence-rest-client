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
package de.itboehmer.confluence.rest.core.cql;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Christian Schulze (c.schulze@micromata.de)
 * @author Martin Böhmer
 */
public class CqlBuilder {

    public static final String WHITESPACE = " ";

    private StringBuffer jql = null;

    public CqlBuilder() {
        jql = new StringBuffer();
    }

    public JqlKeyword addCondition(EField field, EOperator operator, String... operand) {
        JqlKeyword jqlKeyword = new JqlKeyword();

        if (field != null) {
            jql.append(field).append(WHITESPACE);
        }
        if (operator != null) {
            jql.append(operator).append(WHITESPACE);
        }
        if (operand != null) {
            if (operand.length > 1) {
                String join = StringUtils.join(operand, ",");
                jql.append("(").append(join).append(") ");
            } else {
                jql.append(operand[0]).append(WHITESPACE);
            }
        }

        return jqlKeyword;
    }

    public CqlBuilder getCqlBuilder() {
        return this;
    }

    public void clear() {
        jql.setLength(0);
    }

    public class JqlKeyword {

        public CqlBuilder and() {
            jql.append(EKeyword.AND).append(WHITESPACE);
            return getCqlBuilder();
        }

        public CqlBuilder or() {
            jql.append(EKeyword.OR).append(WHITESPACE);
            return getCqlBuilder();
        }

        public String orderBy(SortOrder order, EField... fields) {
            if (fields == null || order == null || fields.length == 0) {
                return build();
            }

            jql.append(EKeyword.ORDER_BY).append(WHITESPACE);
            jql.append(fields[0]);
            jql.append(WHITESPACE + order);

            for (int i = 1; i < fields.length; i++) {
                jql.append(", ");
                jql.append(fields[i]);
                jql.append(WHITESPACE + order);
            }

            return build();
        }

        /**
         * Return the request String and clear the buffer.
         *
         * @return the request string
         */
        public String build() {
            String request = jql.toString();
            clear();
            return request;
        }
    }

    /**
     * Return the request String and clear the buffer.
     *
     * @return the request string
     */
    public String build() {
        String request = jql.toString();
        clear();
        return request;
    }

}
