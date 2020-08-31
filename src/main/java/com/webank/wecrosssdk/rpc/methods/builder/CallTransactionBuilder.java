package com.webank.wecrosssdk.rpc.methods.builder;

import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.TransactionContext;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class CallTransactionBuilder {
    private String transactionID;
    private String path;
    private String account;
    private String method;
    private String[] args;

    public CallTransactionBuilder(String transactionID, String txSeq) {
        this.transactionID = transactionID;
    }

    public static CallTransactionBuilder build() {
        return new CallTransactionBuilder(
                TransactionContext.currentTXID(), TransactionContext.currentSeq());
    }

    public CallTransactionBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CallTransactionBuilder account(String account) {
        this.account = account;
        return this;
    }

    public CallTransactionBuilder method(String method) {
        this.method = method;
        return this;
    }

    public CallTransactionBuilder args(String[] args) {
        this.args = args;
        return this;
    }

    public TransactionResponse send() throws Exception {
        WeCrossService weCrossService = new WeCrossRPCService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossService);
        return weCrossRPC
                .callTransaction(
                        this.transactionID, this.path, this.account, this.method, this.args)
                .send();
    }
}
