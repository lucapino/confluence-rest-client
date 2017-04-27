/**
 * Copyright 2017 Martin Böhmer
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
package de.itboehmer.confluence.rest.core.misc;

import java.net.HttpURLConnection;

/**
 * @author Martin Böhmer (mb@itboehmer.de)
 */
public class UnexpectedContentException extends RestException {

    private static final long serialVersionUID = 141111096347706556L;

    public UnexpectedContentException(String expectedResult, String actualResult) {
        super(HttpURLConnection.HTTP_OK, "OK", null, "Unexpected result. Was: " + actualResult + " Expected: " + expectedResult, null);
    }

}
