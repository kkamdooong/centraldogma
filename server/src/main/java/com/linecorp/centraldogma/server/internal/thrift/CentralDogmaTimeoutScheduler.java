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

package com.linecorp.centraldogma.server.internal.thrift;

import java.util.List;

import com.linecorp.armeria.common.RpcRequest;
import com.linecorp.armeria.common.RpcResponse;
import com.linecorp.armeria.server.Service;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.SimpleDecoratingService;

public final class CentralDogmaTimeoutScheduler extends SimpleDecoratingService<RpcRequest, RpcResponse> {

    public CentralDogmaTimeoutScheduler(Service<RpcRequest, RpcResponse> delegate) {
        super(delegate);
    }

    @Override
    public RpcResponse serve(ServiceRequestContext ctx, RpcRequest req) throws Exception {
        if (ctx.requestTimeoutMillis() > 0) {
            final String method = req.method();
            if ("watchFile".equals(method) || "watchRepository".equals(method)) {
                final List<Object> params = req.params();
                final long timeout = (Long) params.get(params.size() - 1);
                if (timeout > 0) {
                    if (timeout > Long.MAX_VALUE - ctx.requestTimeoutMillis()) {
                        ctx.setRequestTimeoutMillis(0);
                    } else {
                        ctx.setRequestTimeoutMillis(ctx.requestTimeoutMillis() + timeout);
                    }
                }
            }
        }

        return delegate().serve(ctx, req);
    }
}
