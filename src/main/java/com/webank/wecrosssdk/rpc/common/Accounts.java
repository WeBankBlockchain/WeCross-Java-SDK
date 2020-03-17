package com.webank.wecrosssdk.rpc.common;

public class Accounts {
    AccountInfo[] accountInfos;

    static class AccountInfo {
        String name;
        String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public AccountInfo[] getAccountInfos() {
        return accountInfos;
    }

    public void setAccountInfos(AccountInfo[] accountInfos) {
        this.accountInfos = accountInfos;
    }
}
