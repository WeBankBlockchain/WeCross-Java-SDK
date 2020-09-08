package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Accounts;
import com.webank.wecrosssdk.rpc.common.account.UniversalAccount;
import com.webank.wecrosssdk.rpc.methods.Response;

public class AccountResponse extends Response<UniversalAccount> {

    public AccountResponse() {
        super();
    }

    public UniversalAccount getAccount() {
        return getData();
    }

    public void setAccounts(UniversalAccount uaAccounts) {
        setData(uaAccounts);
    }
}
