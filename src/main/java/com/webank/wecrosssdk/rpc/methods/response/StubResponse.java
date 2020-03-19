package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Stubs;
import com.webank.wecrosssdk.rpc.methods.Response;

public class StubResponse extends Response<Stubs> {
    public StubResponse() {
        super();
    }

    public Stubs getStubs() {
        return getData();
    }

    public void setStubs(Stubs stubs) {
        setData(stubs);
    }
}
