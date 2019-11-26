package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.data.CallContractResult;
import com.webank.wecrosssdk.rpc.methods.Response;

public class TransactionResponse extends Response<CallContractResult> {

    public TransactionResponse() {
        super();
    }

    public CallContractResult getCallContractResult() {
        return getData();
    }

    public void setCallContractResult(CallContractResult callContractResult) {
        setData(callContractResult);
    }
}
