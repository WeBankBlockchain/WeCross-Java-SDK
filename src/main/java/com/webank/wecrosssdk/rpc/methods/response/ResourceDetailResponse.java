package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.methods.Response;

public class ResourceDetailResponse extends Response<ResourceDetail> {
    public ResourceDetailResponse() {
        super();
    }

    public ResourceDetail getResourceDetail() {
        return getData();
    }

    public void setResourceDetail(ResourceDetail resourceInfo) {
        setData(resourceInfo);
    }
}
