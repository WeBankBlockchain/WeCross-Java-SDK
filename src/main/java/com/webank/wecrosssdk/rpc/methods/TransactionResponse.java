package com.webank.wecrosssdk.rpc.methods;

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
