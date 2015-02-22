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

/**
 * <p>Measures the Cosine similarity of two CharSequences. It treats the CharSequences as
 * two vectors of an inner product space and compares the angle between them.</p>
 *
 * <p>
 * For further explanation about the Cosine Similarity, take a look at its
 * Wikipedia page at http://en.wikipedia.org/wiki/Cosine_similarity.
 * </p>
 *
 * @since 0.1
 */
public class CosineSimilarity implements StringMetric<Double> {

    @Override
    public Double compare(CharSequence left, CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("String parameters must not be null");
        }
        long dotProduct = dot(left, right);
        double d1 = 0.0d;
        for (int i = 0; i < left.length(); ++i) {
            d1 += Math.pow(((int) left.charAt(i)), 2);
        }
        double d2 = 0.0d;
        for (int i = 0; i < right.length(); ++i) {
            d2 += Math.pow(((int) right.charAt(i)), 2);
        }
        double cosineSimilarity = dotProduct / (double) (Math.sqrt(d1) * Math.sqrt(d2));
        return cosineSimilarity;
    }

    /**
     * Computes the dot product of two CharSequences. It ignores remaining characters. It means
     * that if a string is longer than other, then a smaller part of it will be used to compute
     * the dot product.
     * 
     * @param left left string
     * @param right right string
     * @return the dot product
     */
    protected long dot(CharSequence left, CharSequence right) {
        long dotProduct = 0;
        for (int i = 0; i < left.length() && i < right.length(); ++i) {
            dotProduct += (((int) left.charAt(i)) * ((int) right.charAt(i)));
        }
        return dotProduct;
    }

}
