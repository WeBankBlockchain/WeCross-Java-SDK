package com.webank.wecrosssdk.rpc.common;

import com.webank.wecrosssdk.rpc.common.account.UniversalAccount;

public class UAReceipt {
    private int errorCode = -1;
    private String message;
    private String credential;
    private UniversalAccount universalAccount;

    public UAReceipt(int errorCode, String message, String credential) {
        this.errorCode = errorCode;
        this.message = message;
        this.credential = credential;
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

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
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
        result += credential == null ? "" : ", credential = '" + credential + "'";
        result +=
                universalAccount == null
                        ? ""
                        : ", UniversalAccount = '" + universalAccount.toString() + "'";
        result += "}";
        return result;
    }
}
