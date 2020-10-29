package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Objects;

public class FabricAccount extends ChainAccount {

    private String pubKey;
    private String secKey;
    private String ext;

    public FabricAccount() {
        super();
    }

    public FabricAccount(
            Integer keyID,
            String type,
            boolean isDefault,
            String pubKey,
            String secKey,
            String ext) {
        super(keyID, type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.ext = ext;
    }

    public FabricAccount(String type, String pubKey, String secKey, String ext, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.ext = ext;
    }

    public FabricAccount(Integer keyID, String type, boolean isDefault) {
        super(keyID, type, isDefault);
    }

    public FabricAccount(ChainAccount chainAccount) {
        super(chainAccount.keyID, chainAccount.type, chainAccount.isDefault);
    }

    @JsonSetter("pubKey")
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

    @JsonSetter("secKey")
    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    @JsonGetter("ext")
    public String getExt() {
        return ext;
    }

    @JsonSetter("ext")
    public void setExt(String ext) {
        this.ext = ext;
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
                + ext
                + '\"'
                + ", \"isDefault\":\""
                + isDefault
                + "\"}";
    }

    @Override
    public String toFormatString() {
        return "\t"
                + type
                + " Account:\n"
                + "\tkeyID    : "
                + keyID
                + "\n"
                + "\ttype     : "
                + type
                + "\n"
                + "\tMembershipID : "
                + ext
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n\t----------\n";
    }

    @Override
    public String toDetailString() {
        return "\t"
                + type
                + " Account:\n"
                + "\tkeyID    : "
                + keyID
                + "\n"
                + "\ttype     : "
                + type
                + "\n"
                + "\tcert     : "
                + pubKey
                + "\n"
                + "\tMembershipID : "
                + ext
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n\t----------\n";
    }
}
