package com.webank.wecrosssdk.rpc.methods.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecrosssdk.rpc.common.account.BCOSAccount;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.common.account.FabricAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UARequest {
    private String username;
    private String password;
    private String clientType = "sdk";
    private String authCode = "";
    private ChainAccount chainAccount;

    @JsonIgnore private Logger logger = LoggerFactory.getLogger(UARequest.class);

    public UARequest(String username, String password, ChainAccount chainAccount) {
        this.username = username;
        this.password = password;
        this.chainAccount = chainAccount;
    }

    public UARequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UARequest(String type, ChainAccount chainAccount) {
        switch (type) {
            case "BCOS3.0":
            case "GM_BCOS3.0":
            case "GM_BCOS2.0":
            case "BCOS2.0":
                {
                    this.chainAccount = new BCOSAccount(chainAccount);
                    logger.debug("this.chainAccount is {}", this.chainAccount);
                    break;
                }
            case "Fabric1.4":
            case "Fabric2.0":
                {
                    this.chainAccount = new FabricAccount(chainAccount);
                    logger.debug("this.chainAccount is {}", this.chainAccount);
                    break;
                }
            default:
                {
                    logger.warn("type {} is not support now!", type);
                }
        }
    }

    public UARequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public ChainAccount getChainAccount() {
        return chainAccount;
    }

    public void setChainAccount(ChainAccount chainAccount) {
        this.chainAccount = chainAccount;
    }

    @Override
    public String toString() {
        String result =
                "UARequest{"
                        + "username='"
                        + username
                        + '\''
                        + ", password='"
                        + password
                        + '\''
                        + ", clientType='"
                        + clientType
                        + '\''
                        + ", authCode='"
                        + authCode
                        + '\'';

        result +=
                (chainAccount == null) ? "" : ", chainAccount= '" + chainAccount.toString() + '\'';
        result += "}";
        return result;
    }
}
