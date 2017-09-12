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
 * Date: 04.07.2016
 * Project: ConfluenceTransferPlugin
 */
public enum EField {

    ANCESTOR("ancestor", EFieldType.CONTENT),

    CONTAINER("container", EFieldType.CONTENT),

    CONTENT("content", EFieldType.CONTENT),

    CREATED("created", EFieldType.DATE),

    CREATOR("creator", EFieldType.USER),

    CONTRIBUTOR("contributor", EFieldType.USER),

    FAVOURITE("favourite", EFieldType.USER),

    ID("id", EFieldType.CONTENT),

    LABEL("label", EFieldType.STRING),

    LASTMODIFIED("lastmodified", EFieldType.DATE),

    MACRO("marco", EFieldType.STRING),

    MENTION("mention", EFieldType.USER),

    PARENT("parent", EFieldType.CONTENT),

    SPACE("space", EFieldType.SPACE),

    TEXT("text", EFieldType.TEXT),

    TITLE("title", EFieldType.TEXT),

    TYPE("type", EFieldType.TYPE),

    WATCHER("watcher", EFieldType.USER);
    /**
     * The name of the field.
     */
    private final String field;

    /**
     * The type of the field.
     */
    private final EFieldType type;

    /**
     * Instantiates a new field.
     *
     * @param field = name of the field
     * @param type  = type of the field
     */
    EField(String field, EFieldType type) {
        this.field = field;
        this.type = type;
    }

    /**
     * Gets the name of the field.
     *
     * @return the field name
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the type of the field.
     *
     * @return the field type
     */
    public EFieldType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getField();
    }
}
