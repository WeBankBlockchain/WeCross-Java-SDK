package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Resources;
import com.webank.wecrosssdk.rpc.methods.Response;

public class ResourceResponse extends Response<Resources> {

    public ResourceResponse() {
        super();
    }

    public Resources getResources() {
        return getData();
    }

    public void setResources(Resources resources) {
        setData(resources);
    }
}
