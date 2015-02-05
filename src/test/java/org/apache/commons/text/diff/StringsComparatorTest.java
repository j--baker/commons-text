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
package org.apache.commons.text.diff;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringsComparatorTest {

    private List<String> before;
    private List<String> after;
    private int[]        length;

    @Test
    public void testLength() {
        for (int i = 0; i < before.size(); ++i) {
            final StringsComparator comparator =  new StringsComparator(before.get(i), after.get(i));
            Assert.assertEquals(length[i], comparator.getScript().getModifications());
        }
    }

    @Test
    public void testExecution() {
        for (int i = 0; i < before.size(); ++i) {
            final ExecutionVisitor<Character> ev = new ExecutionVisitor<Character>();
            new StringsComparator(before.get(i), after.get(i)).getScript().visit(ev);
            Assert.assertEquals(after.get(i), ev.getString());
        }
    }

    private class ExecutionVisitor<T> implements CommandVisitor<T> {

        private StringBuilder v;

        public ExecutionVisitor() {
            v = new StringBuilder();
        }

        public void visitInsertCommand(final T object) {
            v.append(object);
        }

        public void visitKeepCommand(final T object) {
            v.append(object);
        }

        public void visitDeleteCommand(final T object) {
        }

        public String getString() {
            return v.toString();
        }

    }

    @Before
    public void setUp() {

        before = Arrays.asList(
            "bottle",
            "nematode knowledge",
            "",
            "aa",
            "prefixed string",
            "ABCABBA",
            "glop glop",
            "coq",
            "spider-man");

        after = Arrays.asList(
            "noodle",
            "empty bottle",
            "",
            "C",
            "prefix",
            "CBABAC",
            "pas glop pas glop",
            "ane",
            "klingon");

        length = new int[] {
            6,
            16,
            0,
            3,
            9,
            5,
            8,
            6,
            13
        };

    }

    @After
    public void tearDown() {
        before = null;
        after  = null;
        length = null;
    }

}
