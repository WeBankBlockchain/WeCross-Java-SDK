package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Accounts;
import com.webank.wecrosssdk.rpc.methods.Response;

public class AccountResponse extends Response<Accounts> {

    public AccountResponse() {
        super();
    }

    public Accounts getAccounts() {
        return getData();
    }

    public void setAccounts(Accounts accounts) {
        setData(accounts);
    }
}
