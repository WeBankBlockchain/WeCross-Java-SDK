package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.UAReceipt;
import com.webank.wecrosssdk.rpc.methods.Response;

public class UAResponse extends Response<UAReceipt> {

    public UAResponse() {
        super();
    }

    public UAReceipt getUAReceipt() {
        return getData();
    }

    public void setUAReceipt(UAReceipt uaLoginReceipt) {
        setData(uaLoginReceipt);
    }
}
