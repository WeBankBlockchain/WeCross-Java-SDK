package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Objects;

public class FabricAccount extends ChainAccount {

    private String pubKey;
    private String secKey;

    public FabricAccount() {
        super();
    }

    public FabricAccount(
            Integer keyID, String type, boolean isDefault, String pubKey, String secKey) {
        super(keyID, type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
    }

    public FabricAccount(String type, String pubKey, String secKey, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
    }

    public FabricAccount(Integer keyID, String type, boolean isDefault) {
        super(keyID, type, isDefault);
    }

    public FabricAccount(ChainAccount chainAccount) {
        super(chainAccount.keyID, chainAccount.type, chainAccount.isDefault);
    }

    @JsonSetter(value = "cert")
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FabricAccount)) return false;
        FabricAccount that = (FabricAccount) o;
        return Objects.equal(pubKey, that.pubKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, pubKey, secKey);
    }

    @Override
    public String toString() {
        return "{"
                + "\"keyID\":\""
                + keyID
                + '\"'
                + ", \"type\":\""
                + type
                + '\"'
                + ", \"cert\":\""
                + pubKey
                + '\"'
                + ", \"isDefault\":\""
                + isDefault
                + "\"}";
    }

    public String toFormatString() {
        return type
                + "Account:\n"
                + "\tkeyID: "
                + keyID
                + "\n"
                + "\ttype: "
                + type
                + "\n"
                + "\tcert: "
                + pubKey
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
