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
package de.itboehmer.confluence.rest.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Helper class for handling Confluence's storage format.
 *
 * @author Martin Böhmer
 */
public final class StorageFormatHelper {

    private static final Logger LOG = LoggerFactory.getLogger(StorageFormatHelper.class);

    public static boolean equalsIgnoreWhitespace(String str1, String str2) {
        // Handle null values
        if ((str1 == null && str2 == null)) {
            return true;
        }
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        // Quick check
        if (str1.equals(str2)) {
            return true;
        }
        // Compare
        String strippedStr1 = str1.replaceAll("\\s", "");
        String strippedStr2 = str2.replaceAll("\\s", "");
        return strippedStr1.equals(strippedStr2);
    }

    public static boolean xmlEquals(String xml1, String xml2) {
        // Handle null values
        if ((xml1 == null && xml2 == null)) {
            return true;
        }
        if (xml1 == null) {
            xml1 = "";
        }
        if (xml2 == null) {
            xml2 = "";
        }
        // Quick check
        if (xml1.equals(xml2)) {
            return true;
        }
        // Compare XML
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc1 = db.parse(new ByteArrayInputStream(xml1.getBytes()));
            doc1.normalizeDocument();

            Document doc2 = db.parse(new ByteArrayInputStream(xml2.getBytes()));
            doc2.normalizeDocument();

            return doc1.isEqualNode(doc2);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOG.error("Error comparing XML strings.\n--XML1--\n" + xml1 + "\n--XML2--\n" + xml2, ex);
        }

        return false;
    }

}
