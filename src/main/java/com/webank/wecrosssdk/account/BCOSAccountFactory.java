package com.webank.wecrosssdk.account;

import com.webank.wecrosssdk.config.Default;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.utils.ConfigUtils;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import org.fisco.bcos.channel.client.P12Manager;
import org.fisco.bcos.channel.client.PEMManager;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCOSAccountFactory {
    static Logger logger = LoggerFactory.getLogger(BCOSAccountFactory.class);

    public static BCOSAccount build(String name, String accountPath) throws Exception {
        Credentials credentials = buildCredentials(accountPath);
        BCOSAccount account = new BCOSAccount(name, credentials);
        return account;
    }

    public static BCOSGMAccount buildGM(String name, String accountPath) throws Exception {
        Credentials credentials = buildCredentials(accountPath);
        BCOSGMAccount account = new BCOSGMAccount(name, credentials);
        return account;
    }

    public static Credentials buildCredentials(String accountPath) throws Exception {
        String accountConfigFile = accountPath + File.separator + Default.ACCOUNT_CONFIG_NAME;
        Map<String, String> accountConfig =
                (Map<String, String>) ConfigUtils.getTomlMap(accountConfigFile).get("account");

        String accountFile = accountPath + File.separator + accountConfig.get("accountFile");
        if (accountFile == null) {
            String errorMessage =
                    "\"accountFile\" in [account] item not found, please check "
                            + accountConfigFile;
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }

        Credentials credentials;
        if (accountFile.contains(".pem")) {
            credentials = loadPemAccount(accountFile);
        } else if (accountFile.contains(".p12")) {
            String password = accountConfig.get("password");
            if (password == null) {
                String errorMessage =
                        "\"password\" in [account] item  not found, please check "
                                + accountConfigFile;
                throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
            }
            credentials = loadP12Account(accountFile, password);
        } else {
            String errorMessage = "Unsupported account file";
            throw new WeCrossSDKException(ErrorCode.UNEXPECTED_CONFIG, errorMessage);
        }
        return credentials;
    }

    // load pem account file
    public static Credentials loadPemAccount(String keyPath)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
                    NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        PEMManager pem = new PEMManager();
        pem.setPemFile(keyPath);
        pem.load();
        ECKeyPair keyPair = pem.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));

        logger.info("BCOS account address: {}", credentials.getAddress());
        return credentials;
    }

    // load p12 account file
    public static Credentials loadP12Account(String keyPath, String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
                    NoSuchProviderException, InvalidKeySpecException, UnrecoverableKeyException {
        P12Manager p12Manager = new P12Manager();
        p12Manager.setP12File(keyPath);
        p12Manager.setPassword(password);
        p12Manager.load();
        ECKeyPair keyPair = p12Manager.getECKeyPair();
        Credentials credentials = GenCredential.create(keyPair.getPrivateKey().toString(16));

        logger.info("BCOS account address: {}", credentials.getAddress());
        return credentials;
    }
}
