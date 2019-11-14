package com.webank.wecrosssdk.rpc.methods;

public class ResourcesRequest {

    boolean ignoreRemote;

    public ResourcesRequest(boolean ignoreRemote) {
        this.ignoreRemote = ignoreRemote;
    }

    public boolean isIgnoreRemote() {
        return ignoreRemote;
    }

    public void setIgnoreRemote(boolean ignoreRemote) {
        this.ignoreRemote = ignoreRemote;
    }
}
