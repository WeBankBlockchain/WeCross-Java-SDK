package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.data.Resources;
import com.webank.wecrosssdk.rpc.methods.Response;

public class ResourcesResponse extends Response<Resources> {

    public ResourcesResponse() {
        super();
    }

    public Resources getResources() {
        return getData();
    }

    public void SetResources(Resources resources) {
        setData(resources);
    }
}
