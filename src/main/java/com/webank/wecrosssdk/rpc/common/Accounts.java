package com.webank.wecrosssdk.rpc.common;

import java.util.List;
import java.util.Map;

public class Accounts {

    List<Map<String, String>> accountInfos;

    public List<Map<String, String>> getAccountInfos() {
        return accountInfos;
    }

    public void setAccountInfos(List<Map<String, String>> accountInfos) {
        this.accountInfos = accountInfos;
    }

    @Override
    public String toString() {
        return "Accounts{" + "accountInfos=" + accountInfos + '}';
    }
}
