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
package de.itboehmer.confluence.rest.core.domain.space;

import com.google.gson.annotations.Expose;
import de.itboehmer.confluence.rest.core.domain.common.PlainBean;

/**
 * Author: Christian Schulze (c.schulze@micromata.de)
 * Date: 04.07.2016
 * Project: ConfluenceTransferPlugin
 */
public class DescriptionBean {

    @Expose
    private PlainBean plain;

    public PlainBean getPlain() {
        return plain;
    }

    public void setPlain(PlainBean plain) {
        this.plain = plain;
    }
}
