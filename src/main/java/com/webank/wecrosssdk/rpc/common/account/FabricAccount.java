package com.webank.wecrosssdk.rpc.common.account;

import com.google.common.base.Objects;

public class FabricAccount extends ChainAccount {

    private String cert;
    private String key;

    public FabricAccount() {
        super();
    }

    public FabricAccount(String type, String UAProof, boolean isDefault, String cert, String key) {
        super(type, UAProof, isDefault);
        this.cert = cert;
        this.key = key;
    }

    public FabricAccount(String type, String cert, String key, boolean isDefault) {
        super(type, isDefault);
        this.cert = cert;
        this.key = key;
    }

    public FabricAccount(ChainAccount chainAccount) {
        super(chainAccount.type, chainAccount.UAProof, chainAccount.isDefault);
    }

    public String getCert() {
        return cert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FabricAccount)) return false;
        FabricAccount that = (FabricAccount) o;
        return Objects.equal(cert, that.cert);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, cert, key);
    }

    @Override
    public String toString() {
        return "{"
                + "\"type\":\""
                + type
                + '\"'
                + ", \"cert\":\""
                + cert
                + '\"'
                + ", \"key\":\""
                + key
                + '\"'
                + ", \"isDefault\":\""
                + isDefault
                + "\"}";
    }

    public String toFormatString() {
        return "FabricAccount:\n"
                + "\ttype: "
                + type
                + "\n"
                + "\tcert: "
                + cert
                + "\n"
                + "\tkey : "
                + key
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
