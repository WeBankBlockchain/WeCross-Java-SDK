package com.webank.wecrosssdk.account;

import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.IdentityFactory;
import org.hyperledger.fabric.sdk.identity.SigningIdentity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

public class FabricAccount implements Account {
    private User user;
    private SigningIdentity signer;

    public FabricAccount(User user) throws Exception {
        this.setUser(user);

        // ECDSA secp256r1
        this.signer =
                IdentityFactory.getSigningIdentity(CryptoSuite.Factory.getCryptoSuite(), user);
    }

    @Override
    public byte[] sign(byte[] message) throws Exception {
        return signer.sign(message);
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getAddress() {
        return user.getAccount();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SigningIdentity getSigner() {
        return signer;
    }
}
