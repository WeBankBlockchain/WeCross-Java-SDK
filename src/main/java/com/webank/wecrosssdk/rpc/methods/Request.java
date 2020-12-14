package com.webank.wecrosssdk.rpc.methods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecrosssdk.rpc.common.Version;
import com.webank.wecrosssdk.rpc.common.WeCrossCallback;

public class Request<T> {
    private String version = Version.CURRENT_VERSION;
    private T data;
    @JsonIgnore private Object ext;

    @JsonIgnore private WeCrossCallback callback;

    public Request() {}

    public Request(T data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public WeCrossCallback getCallback() {
        return callback;
    }

    public void setCallback(WeCrossCallback callback) {
        this.callback = callback;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        String result = "Request{" + "version='" + version + '\'';
        result += (data == null) ? "" : ", data=" + data;
        result += '}';
        return result;
    }
}
