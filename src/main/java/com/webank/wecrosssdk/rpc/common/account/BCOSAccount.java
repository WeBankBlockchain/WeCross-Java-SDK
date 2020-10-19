package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Objects;

public class BCOSAccount extends ChainAccount {

    private String pubKey;
    private String secKey;
    private String ext;

    public BCOSAccount() {
        super();
    }

    public BCOSAccount(
            Integer keyID,
            String type,
            String pubKey,
            String secKey,
            String ext,
            boolean isDefault) {
        super(keyID, type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.ext = ext;
    }

    public BCOSAccount(String type, String pubKey, String secKey, String ext, boolean isDefault) {
        super(type, isDefault);
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.ext = ext;
    }

    public BCOSAccount(Integer keyID, String type, boolean isDefault) {
        super(keyID, type, isDefault);
    }

    public BCOSAccount(ChainAccount chainAccount) {
        super(chainAccount.keyID, chainAccount.type, chainAccount.isDefault);
    }

    @JsonGetter("pubKey")
    public String getPubKey() {
        return pubKey;
    }

    @JsonGetter("secKey")
    public String getSecKey() {
        return secKey;
    }

    @JsonGetter("ext")
    public String getExt() {
        return ext;
    }

    @JsonSetter("pubKey")
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    @JsonSetter("secKey")
    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    @JsonSetter("ext")
    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BCOSAccount)) return false;
        BCOSAccount that = (BCOSAccount) o;
        return Objects.equal(pubKey, that.pubKey) && Objects.equal(ext, that.ext);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, pubKey, ext);
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
                + ext
                + "\""
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
                + "\taddress  : "
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
                + "\tpubKey   : "
                + pubKey
                + "\n"
                + "\taddress  : "
                + ext
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n\t----------\n";
    }
}
