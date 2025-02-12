/**
 * Copyright 2020 Alibaba Group Holding Limited.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.graphscope.groot.store;

import com.alibaba.graphscope.groot.CompletionCallback;
import com.alibaba.maxgraph.proto.groot.*;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Map;

public class StoreIngestService extends StoreIngestGrpc.StoreIngestImplBase {

    private StoreService storeService;

    public StoreIngestService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public void storeIngest(
            StoreIngestRequest request, StreamObserver<StoreIngestResponse> responseObserver) {
        String dataPath = request.getDataPath();
        Map<String, String> config = request.getConfigMap();
        this.storeService.ingestData(
                dataPath,
                config,
                new CompletionCallback<Void>() {
                    @Override
                    public void onCompleted(Void res) {
                        responseObserver.onNext(StoreIngestResponse.newBuilder().build());
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void onError(Throwable t) {
                        responseObserver.onError(
                                Status.INTERNAL
                                        .withDescription(t.getMessage())
                                        .asRuntimeException());
                    }
                });
    }

    @Override
    public void storeClearIngest(
            StoreClearIngestRequest request,
            StreamObserver<StoreClearIngestResponse> responseObserver) {
        try {
            this.storeService.clearIngest();
            responseObserver.onNext(StoreClearIngestResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (IOException e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
