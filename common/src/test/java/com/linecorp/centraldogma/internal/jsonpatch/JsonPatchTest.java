/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: https://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.linecorp.centraldogma.internal.jsonpatch;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import java.io.IOException;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableList;

public final class JsonPatchTest
{
    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private JsonPatchOperation op1;
    private JsonPatchOperation op2;

    @BeforeMethod
    public void init()
    {
        op1 = mock(JsonPatchOperation.class);
        op2 = mock(JsonPatchOperation.class);
    }

    @Test
    public void nullInputsDuringBuildAreRejected()
        throws IOException
    {
        try {
            JsonPatch.fromJson(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void cannotPatchNull()
        throws JsonPatchException
    {
        final JsonPatch patch = new JsonPatch(ImmutableList.of(op1, op2));

        try {
            patch.apply(null);
            fail("No exception thrown!!");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void operationsAreCalledInOrder()
        throws JsonPatchException
    {
        final JsonNode node1 = FACTORY.textNode("hello");
        final JsonNode node2 = FACTORY.textNode("world");

        when(op1.apply(node1)).thenReturn(node2);

        final JsonPatch patch = new JsonPatch(ImmutableList.of(op1, op2));

        final ArgumentCaptor<JsonNode> captor
            = ArgumentCaptor.forClass(JsonNode.class);

        patch.apply(node1);
        verify(op1, only()).apply(same(node1));
        verify(op2, only()).apply(captor.capture());

        assertSame(captor.getValue(), node2);
    }

    @Test
    public void whenOneOperationFailsNextOperationIsNotCalled()
        throws JsonPatchException
    {
        final String message = "foo";
        when(op1.apply(any(JsonNode.class)))
            .thenThrow(new JsonPatchException(message));

        final JsonPatch patch = new JsonPatch(ImmutableList.of(op1, op2));

        try {
            patch.apply(FACTORY.nullNode());
            fail("No exception thrown!!");
        } catch (JsonPatchException e) {
            assertEquals(e.getMessage(), message);
        }

        verifyZeroInteractions(op2);
    }
}
