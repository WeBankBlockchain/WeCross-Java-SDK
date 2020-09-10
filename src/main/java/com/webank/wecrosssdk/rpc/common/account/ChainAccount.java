package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(
        value = {
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "BCOS2.0"),
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "GM_BCOS2.0"),
            @JsonSubTypes.Type(value = FabricAccount.class, name = "Fabric1.4")
        })
public class ChainAccount {
    public String type;
    public String UAProof;
    public boolean isDefault = false;

    public ChainAccount() {}

    public ChainAccount(String type, String UAProof, boolean isDefault) {
        this.type = type;
        this.UAProof = UAProof;
        this.isDefault = isDefault;
    }

    public ChainAccount(String type, boolean isDefault) {
        this.type = type;
        this.isDefault = isDefault;
    }

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

    public String toFormatString() {
        return "ChainAccount\n" + "\ttype: " + type + "\n" + "\tisDefault: " + isDefault + "\n";
    }
}
