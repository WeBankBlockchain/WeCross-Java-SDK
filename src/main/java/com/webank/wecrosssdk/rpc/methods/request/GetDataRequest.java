package com.webank.wecrosssdk.rpc.methods.request;

public class GetDataRequest {

    private String key;

    public GetDataRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
