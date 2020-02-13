package com.webank.wecrosssdk.account;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.sm2.SM2Sign;

public class BCOSGMAccount extends BCOSAccount {
    private String gmAddress;

    public BCOSGMAccount(String name, Credentials innerBCOSCredentials) {
        super(name, innerBCOSCredentials);
        // SM2
        super.signer = new SM2Sign();
    }
}
