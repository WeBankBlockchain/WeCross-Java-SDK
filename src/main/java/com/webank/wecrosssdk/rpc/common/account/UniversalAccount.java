package com.webank.wecrosssdk.rpc.common.account;

import com.google.common.base.Objects;
import java.util.List;

public class UniversalAccount {

    // TODO: 确定名字
    private String name;
    private String password;
    private String pubKey;
    private String secKey;
    private String uaid;
    private List<ChainAccount> chainAccounts;

    public UniversalAccount(
            String name,
            String password,
            String pubKey,
            String secKey,
            String uaid,
            List<ChainAccount> chainAccounts) {
        this.name = name;
        this.password = password;
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.uaid = uaid;
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

    public String getUaid() {
        return uaid;
    }

    public void setUaid(String uaid) {
        this.uaid = uaid;
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
                && Objects.equal(uaid, that.uaid);
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
                + uaid
                + '\''
                + ", chainAccounts="
                + chainAccounts
                + '}';
    }
}
