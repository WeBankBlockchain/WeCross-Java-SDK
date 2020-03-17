package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.methods.Response;

public class TransactionResponse extends Response<Receipt> {

    public TransactionResponse() {
        super();
    }

    public Receipt getReceipt() {
        return getData();
    }

    public void setReceipt(Receipt receipt) {
        setData(receipt);
    }
}
