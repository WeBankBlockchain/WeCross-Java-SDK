package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Stubs;
import com.webank.wecrosssdk.rpc.methods.Response;

public class StubResponse extends Response<Stubs> {
    public StubResponse() {
        super();
    }

    public Stubs getResources() {
        return getData();
    }

    public void SetResources(Stubs stubs) {
        setData(stubs);
    }
}
