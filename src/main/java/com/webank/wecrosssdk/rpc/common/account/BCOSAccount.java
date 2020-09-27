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
            Integer keyID,
            String type,
            String pubKey,
            String secKey,
            String address,
            boolean isDefault) {
        super(keyID, type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.address = address;
    }

    public BCOSAccount(
            String type, String pubKey, String secKey, String address, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.address = address;
    }

    public BCOSAccount(Integer keyID, String type, boolean isDefault) {
        super(keyID, type, isDefault);
    }

    public BCOSAccount(ChainAccount chainAccount) {
        super(chainAccount.keyID, chainAccount.type, chainAccount.isDefault);
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
                + "\"keyID\":\""
                + keyID
                + "\""
                + ", \"type\":\""
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

    @Override
    public String toFormatString() {
        return type
                + " Account:\n"
                + "\tkeyID    : "
                + keyID
                + "\n"
                + "\ttype     : "
                + type
                + "\n"
                + "\tpubKey   :\n"
                + pubKey
                + "\n"
                + "\taddress  : "
                + address
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
