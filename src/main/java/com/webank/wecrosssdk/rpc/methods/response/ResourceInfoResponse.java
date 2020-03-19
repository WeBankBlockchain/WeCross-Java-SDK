package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.ResourceInfo;
import com.webank.wecrosssdk.rpc.methods.Response;

public class ResourceInfoResponse extends Response<ResourceInfo> {
    public ResourceInfoResponse() {
        super();
    }

    public ResourceInfo getResourceInfo() {
        return getData();
    }

    public void setResourceInfo(ResourceInfo resourceInfo) {
        setData(resourceInfo);
    }
}
