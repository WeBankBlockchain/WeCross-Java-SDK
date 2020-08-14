package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class RoutineResponse extends Response<Integer> {
    public RoutineResponse() {
        super();
    }

    public int getResult() {
        return getData();
    }

    public void setResult(int res) {
        setData(res);
    }
}
