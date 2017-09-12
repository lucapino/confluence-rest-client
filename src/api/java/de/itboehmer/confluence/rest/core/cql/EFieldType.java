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

import java.util.Arrays;
import java.util.List;

import static  de.itboehmer.confluence.rest.core.cql.EOperator.*;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 06.07.2016
 * Project: ConfluenceTransferPlugin
 */
public enum EFieldType {

    CONTENT(EQUALS, NOT_EQUALS, IN, NOT_IN),

    DATE(EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN, LESS_THAN_EQUALS),

    USER(EQUALS, NOT_EQUALS, IN, NOT_IN),

    STRING(EQUALS, NOT_EQUALS, IN, NOT_IN),

    SPACE(EQUALS, NOT_EQUALS, IN, NOT_IN),

    TEXT(CONTAINS, DOES_NOT_CONTAIN),

    TYPE(EQUALS, NOT_EQUALS, IN, NOT_IN);


    /**
     * List of supported operators for a type of field.
     */
    private final List<EOperator> supportedOperators;

    /**
     * Instantiates a new field type.
     *
     * @param supportedOperators = the supported operators for a type
     */
    EFieldType(EOperator... supportedOperators) {
        this.supportedOperators = Arrays.asList(supportedOperators);
    }

    /**
     * Gets the list of supported opperators.
     *
     * @return the supported opperators
     */
    public List<EOperator> getSupportedOperators() {
        return supportedOperators;
    }
}
