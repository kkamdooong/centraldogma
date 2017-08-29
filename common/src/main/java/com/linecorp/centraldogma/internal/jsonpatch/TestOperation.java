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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Equivalence;

import com.linecorp.centraldogma.internal.jsonpatch.utils.JsonNumEquals;

/**
 * JSON Patch {@code test} operation
 *
 * <p>The two arguments for this operation are the pointer containing the value
 * to test ({@code path}) and the value to test equality against ({@code
 * value}).</p>
 *
 * <p>It is an error if no value exists at the given path.</p>
 *
 * <p>Also note that equality as defined by JSON Patch is exactly the same as it
 * is defined by JSON Schema itself. As such, this operation reuses {@link
 * JsonNumEquals} for testing equality.</p>
 */
public final class TestOperation
    extends PathValueOperation
{
    private static final Equivalence<JsonNode> EQUIVALENCE
        = JsonNumEquals.getInstance();

    @JsonCreator
    public TestOperation(@JsonProperty("path") final JsonPointer path,
        @JsonProperty("value") final JsonNode value)
    {
        super("test", path, value);
    }

    @Override
    public JsonNode apply(final JsonNode node)
        throws JsonPatchException
    {
        final JsonNode tested = node.at(path);
        if (tested.isMissingNode())
            throw new JsonPatchException("non-existent path: " + path);
        if (!EQUIVALENCE.equivalent(tested, value))
            throw new JsonPatchException("mismatching value: " + tested + " (expected: " + value + ')');
        return node.deepCopy();
    }
}
