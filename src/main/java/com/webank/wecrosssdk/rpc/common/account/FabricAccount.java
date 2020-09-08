package com.webank.wecrosssdk.rpc.common.account;

import com.google.common.base.Objects;

public class FabricAccount extends ChainAccount {

    private String cert;

    public FabricAccount() {
        super();
    }

    public FabricAccount(String type, String UAProof, boolean isDefault, String cert) {
        super(type, UAProof, isDefault);
        this.cert = cert;
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
        return Objects.hashCode(type, cert);
    }

    @Override
    public String toString() {
        return "FabricAccount{"
                + "cert='"
                + cert
                + '\''
                + ", type='"
                + type
                + '\''
                + ", UAProof='"
                + UAProof
                + '\''
                + ", isDefault="
                + isDefault
                + '}';
    }
}
