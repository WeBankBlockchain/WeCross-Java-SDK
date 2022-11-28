package com.webank.wecrosssdk.rpc.common.account;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true,
        include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
        value = {
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "BCOS3.0"),
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "GM_BCOS3.0"),
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "BCOS2.0"),
            @JsonSubTypes.Type(value = BCOSAccount.class, name = "GM_BCOS2.0"),
            @JsonSubTypes.Type(value = FabricAccount.class, name = "Fabric1.4"),
            @JsonSubTypes.Type(value = FabricAccount.class, name = "Fabric2.0")
        })
public class ChainAccount {
    public Integer keyID;
    public String type;
    public boolean isDefault = false;

    public ChainAccount() {}

    public ChainAccount(Integer keyID, String type, boolean isDefault) {
        this.keyID = keyID;
        this.type = type;
        this.isDefault = isDefault;
    }

    public ChainAccount(String type, boolean isDefault) {
        this.type = type;
        this.isDefault = isDefault;
    }

    public Integer getKeyID() {
        return keyID;
    }

    public void setKeyID(Integer keyID) {
        this.keyID = keyID;
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    @JsonGetter("isDefault")
    public boolean isDefault() {
        return isDefault;
    }

    @JsonSetter("isDefault")
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "{"
                + "keyID='"
                + keyID
                + '\''
                + ", type='"
                + type
                + '\''
                + ", isDefault="
                + isDefault
                + '}';
    }

    public String toFormatString() {
        return "ChainAccount\n"
                + "\tkeyID: "
                + keyID
                + "\n"
                + "\ttype: "
                + type
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }

    public String toDetailString() {
        return "ChainAccount\n"
                + "\tkeyID: "
                + keyID
                + "\n"
                + "\ttype: "
                + type
                + "\n"
                + "\tisDefault: "
                + isDefault
                + "\n";
    }
}
