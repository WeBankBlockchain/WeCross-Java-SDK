package com.webank.wecrosssdk.account;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECDSASign;
import org.fisco.bcos.web3j.crypto.Sign;
import org.fisco.bcos.web3j.crypto.SignInterface;

public class BCOSAccount implements Account {
    private org.fisco.bcos.web3j.crypto.Credentials innerBCOSCredentials;
    private String name;

    protected SignInterface signer;

    public BCOSAccount(String name, org.fisco.bcos.web3j.crypto.Credentials innerBCOSCredentials) {
        this.setInnerBCOSCredentials(innerBCOSCredentials);
        this.setName(name);

        // ECDSA secp256k1
        signer = new ECDSASign();
    }

    @Override
    public byte[] sign(byte[] message) throws Exception {
        Sign.SignatureData signData =
                signer.signMessage(message, innerBCOSCredentials.getEcKeyPair());
        byte[] encodedBytes = BCOSSignatureEncoder.encode(signData);

        return encodedBytes;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAddress() {
        return innerBCOSCredentials.getAddress();
    }

    public void setInnerBCOSCredentials(Credentials innerBCOSCredentials) {
        this.innerBCOSCredentials = innerBCOSCredentials;
    }

    public org.fisco.bcos.web3j.crypto.Credentials getInnerBCOSCredentials() {
        return this.innerBCOSCredentials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SignInterface getSigner() {
        return signer;
    }
}
