package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class RoutineInfoResponse extends Response<String> {
    public RoutineInfoResponse() {
        super();
    }

    public String getInfo() {
        return getData();
    }

    public void setInfo(String info) {
        setData(info);
    }
}
