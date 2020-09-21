package com.webank.wecrosssdk.rpc.common.account;

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

    public String getPubKey() {
        return pubKey;
    }

    public String getSecKey() {
        return secKey;
    }

    public String getExt() {
        return ext;
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
                + ", \"ext\":\""
                + ext
                + "\""
                + ", \"isDefault\":\""
                + isDefault
                + "\"}";
    }

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
                + "\text  : "
                + ext
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
