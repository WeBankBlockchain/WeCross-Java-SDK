package com.webank.wecrosssdk.account;

import java.util.Map;

public class Accounts {
    private Map<String, Account> accountsMap;

    Accounts(Map<String, Account> accountsMap) {
        this.accountsMap = accountsMap;
    }

    public Account getAccount(String name) throws Exception {
        if (!accountsMap.containsKey(name)) {
            return null;
        }
        return accountsMap.get(name);
    }

    public int size() {
        return accountsMap.size();
    }
}
