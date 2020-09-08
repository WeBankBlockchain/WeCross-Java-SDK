package com.webank.wecrosssdk.rpc.common.account;

import com.google.common.base.Objects;
import java.util.List;

public class UniversalAccount {

    private String name;
    private String password;
    private String pubKey;
    private String secKey;
    private String uaID;
    private List<ChainAccount> chainAccounts;

    public UniversalAccount(
            String name,
            String password,
            String pubKey,
            String secKey,
            String uaID,
            List<ChainAccount> chainAccounts) {
        this.name = name;
        this.password = password;
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.uaID = uaID;
        this.chainAccounts = chainAccounts;
    }

    public UniversalAccount() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getSecKey() {
        return secKey;
    }

    public String getUaID() {
        return uaID;
    }

    public void setUaID(String uaID) {
        this.uaID = uaID;
    }

    public List<ChainAccount> getChainAccounts() {
        return chainAccounts;
    }

    public void setChainAccounts(List<ChainAccount> chainAccounts) {
        this.chainAccounts = chainAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniversalAccount)) return false;
        UniversalAccount that = (UniversalAccount) o;
        return Objects.equal(name, that.name)
                && Objects.equal(pubKey, that.pubKey)
                && Objects.equal(uaID, that.uaID);
    }

    @Override
    public String toString() {
        return "UniversalAccount{"
                + "name='"
                + name
                + '\''
                + ", password='"
                + password
                + '\''
                + ", pubKey='"
                + pubKey
                + '\''
                + ", secKey='"
                + secKey
                + '\''
                + ", uaID='"
                + uaID
                + '\''
                + ", chainAccounts="
                + chainAccounts
                + '}';
    }
}
