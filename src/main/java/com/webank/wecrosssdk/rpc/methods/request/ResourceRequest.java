package com.webank.wecrosssdk.rpc.methods.request;

public class ResourceRequest {

    boolean ignoreRemote;

    public ResourceRequest() {}

    public ResourceRequest(boolean ignoreRemote) {
        this.ignoreRemote = ignoreRemote;
    }

    public boolean isIgnoreRemote() {
        return ignoreRemote;
    }

    public void setIgnoreRemote(boolean ignoreRemote) {
        this.ignoreRemote = ignoreRemote;
    }

    @Override
    public String toString() {
        return "ResourceRequest{" + "ignoreRemote=" + ignoreRemote + '}';
    }
}
