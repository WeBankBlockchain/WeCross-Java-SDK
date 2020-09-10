package com.webank.wecrosssdk.rpc.common.account;

import com.google.common.base.Objects;

public class BCOSAccount extends ChainAccount {

    private String pubKey;
    private String secKey;
    private String address;

    public BCOSAccount() {
        super();
    }

    public BCOSAccount(
            String type,
            String pubKey,
            String secKey,
            String address,
            String UAProof,
            boolean isDefault) {
        super(type, UAProof, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.address = address;
    }

    public BCOSAccount(String type, String pubKey, String secKey, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
    }

    public BCOSAccount(ChainAccount chainAccount) {
        super(chainAccount.type, chainAccount.UAProof, chainAccount.isDefault);
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getSecKey() {
        return secKey;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BCOSAccount)) return false;
        BCOSAccount that = (BCOSAccount) o;
        return Objects.equal(pubKey, that.pubKey) && Objects.equal(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, pubKey, address);
    }

    @Override
    public String toString() {
        return "{"
                + "\"type\":\""
                + type
                + "\""
                + ", \"pubKey\":\""
                + pubKey
                + "\""
                + ", \"address\":\""
                + address
                + "\""
                + ", \"isDefault\":\""
                + isDefault
                + "\"}";
    }

    public String toFormatString() {
        return "BCOSAccount:\n"
                + "\ttype  : "
                + type
                + "\n"
                + "\tpubKey: "
                + pubKey
                + "\n"
                + "\tkey   : "
                + address
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
