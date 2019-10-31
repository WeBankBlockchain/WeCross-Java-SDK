package com.webank.wecrosssdk.rpc.methods;

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
