package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class TransactionRequest {
    private String sig;
    private String retTypes[];
    private String method;
    private Object args[];

    public TransactionRequest(String sig, String method, Object[] args) {
        this.sig = sig;
        this.method = method;
        this.args = args;
    }

    public TransactionRequest(String sig, String[] retTypes, String method, Object[] args) {
        this.sig = sig;
        this.retTypes = retTypes;
        this.method = method;
        this.args = args;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String[] getRetTypes() {
        return retTypes;
    }

    public void setRetTypes(String[] retTypes) {
        this.retTypes = retTypes;
    }

    public String getMethod() {
        return method;
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

    @Override
    public String toString() {
        return "TransactionRequest{"
                + "sig='"
                + sig
                + '\''
                + ", retTypes="
                + Arrays.toString(retTypes)
                + ", method='"
                + method
                + '\''
                + ", args="
                + Arrays.toString(args)
                + '}';
    }
}
