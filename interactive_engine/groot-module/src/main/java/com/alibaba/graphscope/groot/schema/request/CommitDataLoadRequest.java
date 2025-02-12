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
package com.alibaba.graphscope.groot.schema.request;

import com.alibaba.graphscope.groot.operation.OperationType;
import com.alibaba.maxgraph.proto.CommitDataLoadPb;
import com.alibaba.maxgraph.sdkcommon.common.DataLoadTarget;
import com.google.protobuf.ByteString;

public class CommitDataLoadRequest extends AbstractDdlRequest {

    private DataLoadTarget dataLoadTarget;
    private long tableId;
    private String path;

    public CommitDataLoadRequest(DataLoadTarget dataLoadTarget, long tableId, String path) {
        super(OperationType.COMMIT_DATA_LOAD);
        this.dataLoadTarget = dataLoadTarget;
        this.tableId = tableId;
        this.path = path;
    }

    @Override
    protected ByteString getBytes() {
        return CommitDataLoadPb.newBuilder()
                .setTarget(dataLoadTarget.toProto())
                .setTableIdx(this.tableId)
                .setPath(this.path)
                .build()
                .toByteString();
    }
}
