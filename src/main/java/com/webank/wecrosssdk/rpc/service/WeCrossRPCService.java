package com.webank.wecrosssdk.rpc.service;

import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.config.Default;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Connection;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.utils.ConfigUtils;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class WeCrossRPCService implements WeCrossService {
    private Logger logger = LoggerFactory.getLogger(WeCrossService.class);

    private Connection connection;

    public void init() throws WeCrossSDKException {
        this.connection = getConnection(Default.APPLICATION_CONFIG_FILE);
    }

    private void checkRequest(Request<?> request) throws Exception {
        if (request.getVersion().isEmpty()) {
            throw new Exception("request version is empty");
        }
        if (request.getMethod().isEmpty()) {
            throw new Exception("request method is empty");
        }
    }

    private <T extends Response> void checkResponse(ResponseEntity<T> response) throws Exception {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("method not exists: " + response.toString());
        }
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws Exception {
        String url =
                RPCUtils.pathToUrl(connection.getServer(), request.getPath())
                        + "/"
                        + request.getMethod();
        logger.info("method: {}; url: {}", request.getMethod(), url);

        checkRequest(request);

        RestTemplate restTemplate =
                getRestTemplate(
                        connection.getKeyStoreType(),
                        connection.getKeyStore(),
                        connection.getKeyStorePass(),
                        connection.getTrustStore(),
                        connection.getTrustStorePass());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        ResponseEntity<T> httpResponse =
                restTemplate.exchange(url, HttpMethod.POST, httpRequest, responseType);

        checkResponse(httpResponse);
        T response = httpResponse.getBody();
        logger.info(
                "receive status:{} message:{} data:{}",
                response.getResult(),
                response.getMessage(),
                response.getData());

        if (response.getData() != null) {
            logger.info("response data: {}", response.getData());
        }

        return response;
    }

    private Connection getConnection(String config) throws WeCrossSDKException {
        Toml toml = ConfigUtils.getToml(config);
        Connection connection = new Connection();
        connection.setServer(getServer(toml));
        connection.setKeyStoreType(getKeyStoreType(toml));
        connection.setKeyStore(getKeyStore(toml));
        connection.setKeyStorePass(getKeyStorePass(toml));
        connection.setTrustStore(getTrustStore(toml));
        connection.setTrustStorePass(getTrustStorePass(toml));
        return connection;
    }

    private String getServer(Toml toml) throws WeCrossSDKException {
        String server = toml.getString("connection.server");
        if (server == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.server], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return server;
    }

    private String getKeyStoreType(Toml toml) throws WeCrossSDKException {
        String keyStoreType = toml.getString("connection.keyStoreType");
        if (keyStoreType == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.keyStoreType], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return keyStoreType;
    }

    private String getKeyStore(Toml toml) throws WeCrossSDKException {
        String keyStore = toml.getString("connection.keyStore");
        if (keyStore == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.keyStore], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return keyStore;
    }

    private String getKeyStorePass(Toml toml) throws WeCrossSDKException {
        String keyStorePass = toml.getString("connection.keyStorePass");
        if (keyStorePass == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.keyStorePass], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return keyStorePass;
    }

    private String getTrustStore(Toml toml) throws WeCrossSDKException {
        String trustStore = toml.getString("connection.trustStore");
        if (trustStore == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.trustStore], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return trustStore;
    }

    private String getTrustStorePass(Toml toml) throws WeCrossSDKException {
        String trustStorePass = toml.getString("connection.trustStorePass");
        if (trustStorePass == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.trustStorePass], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return trustStorePass;
    }

    private RestTemplate getRestTemplate(
            String keyStoreType,
            String keyStore,
            String keyStorePass,
            String trustStore,
            String trustStorePass)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
                    KeyStoreException, KeyManagementException, IOException {
        SSLContext sslContext =
                getSSLContext(keyStoreType, keyStore, keyStorePass, trustStore, trustStorePass);
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    private SSLContext getSSLContext(
            String keyStoreType,
            String keyStoreFile,
            String keyStorePass,
            String trustStoreFile,
            String trustStorePass)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException,
                    CertificateException, UnrecoverableKeyException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        KeyStore keyStore =
                getRealKeyStore(
                        keyStoreType,
                        resolver.getResource(keyStoreFile).getInputStream(),
                        keyStorePass);
        KeyManager[] keyManagers = getKeyManager(keyStore, keyStorePass);
        KeyStore trustStore =
                getRealKeyStore(
                        keyStoreType,
                        resolver.getResource(trustStoreFile).getInputStream(),
                        trustStorePass);
        TrustManager[] trustManagers = getTrustManager(trustStore);

        SSLContext context = SSLContext.getInstance(Default.SSL_TYPE);
        context.init(keyManagers, trustManagers, SecureRandom.getInstanceStrong());
        return context;
    }

    private KeyManager[] getKeyManager(KeyStore keyStore, String password)
            throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory factory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, password.toCharArray());
        return factory.getKeyManagers();
    }

    private TrustManager[] getTrustManager(KeyStore trustStore)
            throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory factory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(trustStore);
        return factory.getTrustManagers();
    }

    private KeyStore getRealKeyStore(String keyStoreType, InputStream stream, String password)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(stream, password.toCharArray());
        return keyStore;
    }
}
