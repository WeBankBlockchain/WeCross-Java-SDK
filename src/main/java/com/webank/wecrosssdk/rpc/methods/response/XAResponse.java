package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class XAResponse extends Response<RawXAResponse> {
    public XAResponse() {
        super();
    }

    public RawXAResponse getXARawResponse() {
        return getData();
    }

    public void setXARawResponse(RawXAResponse res) {
        setData(res);
    }
}
