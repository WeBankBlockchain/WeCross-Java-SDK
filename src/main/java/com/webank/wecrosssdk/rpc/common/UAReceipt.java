package com.webank.wecrosssdk.rpc.common;

import com.webank.wecrosssdk.rpc.common.account.UniversalAccount;

public class UAReceipt {
    private int errorCode = -1;
    private String message;
    private String token;
    private UniversalAccount universalAccount;

    public UAReceipt(int errorCode, String message, String token) {
        this.errorCode = errorCode;
        this.message = message;
        this.token = token;
    }

    public UAReceipt(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public UAReceipt() {}

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UniversalAccount getUniversalAccount() {
        return universalAccount;
    }

    public void setUniversalAccount(UniversalAccount universalAccount) {
        this.universalAccount = universalAccount;
    }

    @Override
    public String toString() {

        String result =
                "UAReceipt{" + "errorCode=" + errorCode + ", errorMessage='" + message + "'";
        result += token == null ? "" : ", token = '" + token + "'";
        result +=
                universalAccount == null
                        ? ""
                        : ", UniversalAccount = '" + universalAccount.toString() + "'";
        result += "}";
        return result;
    }
}
