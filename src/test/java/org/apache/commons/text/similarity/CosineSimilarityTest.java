/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.similarity.CosineSimilarity}.
 */
public class CosineSimilarityTest {

    private static CosineSimilarity cosineSimilarity;

    @BeforeClass
    public static void setUp() {
        cosineSimilarity = new CosineSimilarity();
    }

    @Test
    public void testCosineSimilarity() {
        assertEquals(Double.valueOf(0.62d), roundValue(cosineSimilarity.compare("ABCDE", "AB")));
        assertEquals(Double.valueOf(1.00d), roundValue(cosineSimilarity.compare("AB", "AB")));
    }

    // --- Utility methods

    private Double roundValue(Double value) {
        return (Double) new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
