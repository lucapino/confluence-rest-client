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
package com.github.lucapino.confluence.rest.core.api.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link StorageFormatHelper}.
 *
 * @author Martin Böhmer
 */
public class StorageFormatHelperTest {

    @Test
    public void nullgEqualityTest() throws IOException {
        String referenceXhtml = readResource("/xml-reference.xml");
        Assert.assertNotNull(referenceXhtml);

        Assert.assertTrue(StorageFormatHelper.equalsIgnoreWhitespace(null, null));
        Assert.assertFalse(StorageFormatHelper.equalsIgnoreWhitespace(referenceXhtml, null));
        Assert.assertFalse(StorageFormatHelper.equalsIgnoreWhitespace(null, referenceXhtml));
    }

    @Test
    public void stringEqualityTest() throws IOException {
        String referenceXhtml = readResource("/xml-reference.xml");
        String testXml = readResource("/t1-eq.xml");

        Assert.assertEquals(referenceXhtml, testXml);
        Assert.assertTrue(StorageFormatHelper.equalsIgnoreWhitespace(referenceXhtml, testXml));
    }

    @Test
    public void xmlEqualityTest() throws IOException {
        String referenceXhtml = readResource("/xml-reference.xml");
        String testXhtml = readResource("/t2-xml-eq.xml");

        Assert.assertFalse(referenceXhtml.equals(testXhtml));
        Assert.assertTrue(StorageFormatHelper.equalsIgnoreWhitespace(referenceXhtml, testXhtml));
    }

    @Test
    public void xmlNonEqualityTest() throws IOException {
        String referenceXhtml = readResource("/xml-reference.xml");
        String testXhtml = readResource("/t3-neq.xml");

        Assert.assertFalse(referenceXhtml.equals(testXhtml));
        Assert.assertFalse(StorageFormatHelper.equalsIgnoreWhitespace(referenceXhtml, testXhtml));
    }

    private String readResource(String filename) throws IOException {
        InputStream in = getClass().getResourceAsStream(filename);
        return IOUtils.toString(in, "utf8");
    }

}
