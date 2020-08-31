package com.webank.wecrosssdk.rpc.methods.builder;

import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.TransactionContext;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class ExecTransactionBuilder {

    private String transactionID;
    private String txSeq;
    private String path;
    private String account;
    private String method;
    private String[] args;

    public ExecTransactionBuilder(String transactionID, String txSeq) {
        this.transactionID = transactionID;
        this.txSeq = txSeq;
    }

    public static ExecTransactionBuilder build() {
        return new ExecTransactionBuilder(
                TransactionContext.currentTXID(), TransactionContext.currentSeq());
    }

    public ExecTransactionBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ExecTransactionBuilder account(String account) {
        this.account = account;
        return this;
    }

    public ExecTransactionBuilder method(String method) {
        this.method = method;
        return this;
    }

    public ExecTransactionBuilder args(String[] args) {
        this.args = args;
        return this;
    }

    public TransactionResponse send() throws Exception {
        WeCrossService weCrossService = new WeCrossRPCService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossService);
        this.txSeq = TransactionContext.currentSeq();
        return weCrossRPC
                .execTransaction(
                        this.transactionID,
                        this.txSeq,
                        this.path,
                        this.account,
                        this.method,
                        this.args)
                .send();
    }
}
