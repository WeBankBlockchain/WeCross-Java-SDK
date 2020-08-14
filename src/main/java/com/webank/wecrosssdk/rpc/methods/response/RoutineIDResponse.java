package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class RoutineIDResponse extends Response<String[]> {
    public RoutineIDResponse() {
        super();
    }

    public String[] getIDs() {
        return getData();
    }

    public void setIDs(String[] ids) {
        setData(ids);
    }
}
