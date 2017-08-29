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

package com.linecorp.centraldogma.server.admin_v2.service;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;

import com.linecorp.centraldogma.server.command.Command;
import com.linecorp.centraldogma.server.command.CommandExecutor;
import com.linecorp.centraldogma.server.project.ProjectManager;

/**
 * A base service class for CentralDogma admin service.
 */
class AbstractService {

    private final ProjectManager projectManager;
    private final CommandExecutor executor;

    AbstractService(ProjectManager projectManager,
                    CommandExecutor executor) {
        this.projectManager = requireNonNull(projectManager, "projectManager");
        this.executor = requireNonNull(executor, "executor");
    }

    protected final ProjectManager projectManager() {
        return projectManager;
    }

    protected final CommandExecutor executor() {
        return executor;
    }

    protected <T> CompletableFuture<T> execute(Command<T> command) {
        return executor().execute(command);
    }
}
