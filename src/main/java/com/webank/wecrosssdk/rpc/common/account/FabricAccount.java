package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Objects;

public class FabricAccount extends ChainAccount {

    private String pubKey;
    private String secKey;
    private String mspID;

    public FabricAccount() {
        super();
    }

    public FabricAccount(
            Integer keyID,
            String type,
            boolean isDefault,
            String pubKey,
            String secKey,
            String mspID) {
        super(keyID, type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.mspID = mspID;
    }

    public FabricAccount(
            String type, String pubKey, String secKey, String mspID, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.mspID = mspID;
    }

    public FabricAccount(Integer keyID, String type, boolean isDefault) {
        super(keyID, type, isDefault);
    }

    public FabricAccount(ChainAccount chainAccount) {
        super(chainAccount.keyID, chainAccount.type, chainAccount.isDefault);
    }

    @JsonSetter("cert")
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    @JsonGetter("pubKey")
    public String getPubKey() {
        return pubKey;
    }

    @JsonGetter("secKey")
    public String getSecKey() {
        return secKey;
    }

    @JsonGetter("mspID")
    public String getMspID() {
        return mspID;
    }

    @JsonSetter("mspID")
    public void setMspID(String mspID) {
        this.mspID = mspID;
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
                + ", \"MembershipID\":\""
                + mspID
                + '\"'
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
                + "\tcert     :\n"
                + pubKey
                + "\n"
                + "\tMembershipID : "
                + mspID
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
