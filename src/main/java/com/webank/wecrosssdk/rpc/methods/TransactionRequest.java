package com.webank.wecrosssdk.rpc.methods;

import java.util.Arrays;

public class TransactionRequest {
    private String sig;
    private String method;
    private Object args[];

    public TransactionRequest(String sig, String method, Object[] args) {
        this.sig = sig;
        this.method = method;
        this.args = args;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "TransactionRequest [sig="
                + sig
                + ", method="
                + method
                + ", args="
                + Arrays.toString(args)
                + "]";
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object args[]) {
        this.args = args;
    }
}
