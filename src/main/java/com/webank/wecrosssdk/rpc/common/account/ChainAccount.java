package com.webank.wecrosssdk.rpc.common.account;

public abstract class ChainAccount {
    protected String type;
    protected String UAProof;
    public boolean isDefault = false;

    public ChainAccount(String type, String UAProof, boolean isDefault) {
        this.type = type;
        this.UAProof = UAProof;
        this.isDefault = isDefault;
    }

    public ChainAccount() {}

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public String getUAProof() {
        return UAProof;
    }

    protected void setUAProof(String UAProof) {
        this.UAProof = UAProof;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "ChainAccount{"
                + "type='"
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
