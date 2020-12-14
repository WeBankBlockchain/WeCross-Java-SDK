package com.webank.wecrosssdk.rpc.common.account;

import static com.webank.wecrosssdk.utils.RPCUtils.printHexDecently;

import java.util.List;
import java.util.Objects;

public class UniversalAccount {

    private String username;
    private String password;
    private String pubKey;
    private String secKey;
    private String uaID;
    private List<ChainAccount> chainAccounts;

    public UniversalAccount(
            String username,
            String password,
            String pubKey,
            String secKey,
            String uaID,
            List<ChainAccount> chainAccounts) {
        this.username = username;
        this.password = password;
        this.pubKey = pubKey;
        this.secKey = secKey;
        this.uaID = uaID;
        this.chainAccounts = chainAccounts;
    }

    public UniversalAccount(String username, String password, List<ChainAccount> chainAccounts) {
        this.username = username;
        this.password = password;
        this.chainAccounts = chainAccounts;
    }

    public UniversalAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UniversalAccount() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getSecKey() {
        return secKey;
    }

    public String getUaID() {
        return uaID;
    }

    public void setUaID(String uaID) {
        this.uaID = uaID;
    }

    public List<ChainAccount> getChainAccounts() {
        return chainAccounts;
    }

    public void setChainAccounts(List<ChainAccount> chainAccounts) {
        this.chainAccounts = chainAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniversalAccount)) return false;
        UniversalAccount that = (UniversalAccount) o;
        return Objects.equals(username, that.username)
                && Objects.equals(pubKey, that.pubKey)
                && Objects.equals(uaID, that.uaID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        StringBuilder result =
                new StringBuilder(
                        "{"
                                + "\"username\":\""
                                + username
                                + "\""
                                + ", \"pubKey\":\""
                                + pubKey
                                + "\""
                                + ", \"uaID\":\""
                                + uaID
                                + "\"");

        result.append(", \"chainAccounts\": [");
        if (chainAccounts != null && !chainAccounts.isEmpty()) {
            for (ChainAccount chainAccount : chainAccounts) {
                result.append(chainAccount.toString()).append(",");
            }
            result.deleteCharAt(result.lastIndexOf(","));
        }
        result.append("]");
        result.append("}");
        return result.toString();
    }

    public String toFormatString() {
        StringBuilder result = new StringBuilder("Universal Account:\n");
        result.append("username: ").append(username).append("\n");
        result.append("pubKey  : ").append(printHexDecently(pubKey)).append("\n");
        result.append("uaID    : ").append(printHexDecently(uaID)).append("\n");
        if (chainAccounts != null && !chainAccounts.isEmpty()) {
            result.append("chainAccounts: [");
            result.append("\n");
            StringBuilder defaultAccountString = new StringBuilder();
            StringBuilder normalAccountString = new StringBuilder();
            for (ChainAccount chainAccount : chainAccounts) {
                if (chainAccount.isDefault) {
                    defaultAccountString.append(chainAccount.toFormatString());
                } else {
                    normalAccountString.append(chainAccount.toFormatString());
                }
            }
            result.append(defaultAccountString).append(normalAccountString);
            result.append("]");
        }
        return result.toString();
    }

    public String toDetailString() {
        StringBuilder result = new StringBuilder("Universal Account:\n");
        result.append("username: ").append(username).append("\n");
        result.append("pubKey  : ").append(pubKey).append("\n");
        result.append("uaID    : ").append(uaID).append("\n");
        if (chainAccounts != null && !chainAccounts.isEmpty()) {
            result.append("chainAccounts: [");
            result.append("\n");
            StringBuilder defaultAccountString = new StringBuilder();
            StringBuilder normalAccountString = new StringBuilder();
            for (ChainAccount chainAccount : chainAccounts) {
                if (chainAccount.isDefault) {
                    defaultAccountString.append(chainAccount.toDetailString());
                } else {
                    normalAccountString.append(chainAccount.toDetailString());
                }
            }
            result.append(defaultAccountString).append(normalAccountString);
            result.append("]");
        }
        return result.toString();
    }
}
