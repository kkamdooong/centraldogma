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

package com.linecorp.centraldogma.server.repository;

import com.linecorp.centraldogma.common.Revision;
import com.linecorp.centraldogma.server.common.StorageException;

public class RevisionExistsException extends StorageException {

    private static final long serialVersionUID = -6372976353798444205L;

    public RevisionExistsException() {}

    public RevisionExistsException(Revision revision) {
        this(revision.text());
    }

    public RevisionExistsException(String message) {
        super(message);
    }

    public RevisionExistsException(Throwable cause) {
        super(cause);
    }

    public RevisionExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    protected RevisionExistsException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
