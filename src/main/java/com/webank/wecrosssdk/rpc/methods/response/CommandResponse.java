package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class CommandResponse extends Response<String> {
    public CommandResponse() {
        super();
    }

    public String getResult() {
        return getData();
    }

    public void setResult(String res) {
        setData(res);
    }
}
