/*
 * Copyright 2017 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.centraldogma.server.command;

import static com.linecorp.centraldogma.testing.internal.TestUtil.assertJsonConversion;

import org.junit.Test;

public class RemoveRunspaceCommandTest {
    @Test
    public void testJsonConversion() {
        assertJsonConversion(new RemoveRunspaceCommand("foo", "bar", 42),
                             Command.class,
                             '{' +
                             "  \"type\": \"REMOVE_RUNSPACE\"," +
                             "  \"projectName\": \"foo\"," +
                             "  \"repositoryName\": \"bar\"," +
                             "  \"baseRevision\": 42" +
                             '}');
    }
}
