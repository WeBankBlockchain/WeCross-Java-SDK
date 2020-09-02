package com.webank.wecrosssdk.rpc.methods.builder;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.util.Arrays;

public class SendTransactionBuilder {

    private String path;
    private String account;
    private String method;
    private String[] args;

    public SendTransactionBuilder() {}

    public SendTransactionBuilder(String path, String account, String method, String[] args) {
        this.path = path;
        this.account = account;
        this.method = method;
        this.args = args;
    }

    public static SendTransactionBuilder build() {
        return new SendTransactionBuilder();
    }

    public SendTransactionBuilder path(String path) {
        this.path = path;
        return this;
    }

    public SendTransactionBuilder account(String account) {
        this.account = account;
        return this;
    }

    public SendTransactionBuilder method(String method) {
        this.method = method;
        return this;
    }

    public SendTransactionBuilder args(String[] args) {
        this.args = args;
        return this;
    }

    public TransactionResponse send(WeCrossRPC weCrossRPC) throws Exception {
        if (weCrossRPC == null) {
            throw new WeCrossSDKException(
                    ErrorCode.REMOTECALL_ERROR,
                    "SendTransactionBuilder: RPC in send(WeCrossRPC) is null");
        }
        if (this.account == null || this.path == null || this.method == null || this.args == null) {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING, "Some field(s) in ExecTransactionBuilder is null!");
        }
        return weCrossRPC.sendTransaction(this.path, this.account, this.method, this.args).send();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "sendTransactionBuilder{"
                + ", path='"
                + path
                + '\''
                + ", account='"
                + account
                + '\''
                + ", method='"
                + method
                + '\''
                + ", args="
                + Arrays.toString(args)
                + '}';
    }
}
