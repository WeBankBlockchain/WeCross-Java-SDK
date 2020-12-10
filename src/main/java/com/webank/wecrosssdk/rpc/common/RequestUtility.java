package com.webank.wecrosssdk.rpc.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCRest;
import com.webank.wecrosssdk.rpc.methods.response.AuthCodeReceipt;
import com.webank.wecrosssdk.rpc.methods.response.AuthCodeResponse;
import com.webank.wecrosssdk.rpc.methods.response.PubResponse;
import java.security.PublicKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtility {

    private static final Logger logger = LoggerFactory.getLogger(WeCrossRPCRest.class);

    public static String buildLoginParams(WeCrossRPC weCrossRPC, String username, String password)
            throws Exception {
        PubResponse pubResponse = weCrossRPC.queryPub().send();
        AuthCodeResponse authCodeResponse = weCrossRPC.queryAuthCode().send();

        String pub = pubResponse.getData().getPub();
        AuthCodeReceipt.AuthCodeInfo authCode = authCodeResponse.getData().getAuthCode();

        String confusedPassword = DigestUtils.sha256Hex(LoginSalt.LoginSalt + password);

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "login username: {}, pub: {}, randomToken: {}",
                    username,
                    pub,
                    authCode.getRandomToken());
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(confusedPassword);
        loginRequest.setUsername(username);
        loginRequest.setRandomToken(authCode.getRandomToken());

        PublicKey publicKey = RSAUtility.createPublicKey(pub);
        ObjectMapper objectMapper = new ObjectMapper();
        String params =
                RSAUtility.encryptBase64(objectMapper.writeValueAsBytes(loginRequest), publicKey);

        return params;
    }

    public static String buildRegisterParams(
            WeCrossRPC weCrossRPC, String username, String password) throws Exception {
        PubResponse pubResponse = weCrossRPC.queryPub().send();
        AuthCodeResponse authCodeResponse = weCrossRPC.queryAuthCode().send();
        String pub = pubResponse.getData().getPub();
        AuthCodeReceipt.AuthCodeInfo authCode = authCodeResponse.getData().getAuthCode();

        String confusedPassword = DigestUtils.sha256Hex(LoginSalt.LoginSalt + password);

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "register username: {}, pub: {}, randomToken: {}",
                    username,
                    pub,
                    authCode.getRandomToken());
        }

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(confusedPassword);
        registerRequest.setUsername(username);
        registerRequest.setRandomToken(authCode.getRandomToken());

        PublicKey publicKey = RSAUtility.createPublicKey(pub);
        ObjectMapper objectMapper = new ObjectMapper();
        String params =
                RSAUtility.encryptBase64(
                        objectMapper.writeValueAsBytes(registerRequest), publicKey);
        return params;
    }
}
