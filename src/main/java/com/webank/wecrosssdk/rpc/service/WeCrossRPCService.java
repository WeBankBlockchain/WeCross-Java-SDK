package com.webank.wecrosssdk.rpc.service;

import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.common.ConfigDefault;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Connection;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.utils.ConfigUtils;
import com.webank.wecrosssdk.utils.KeyCertLoader;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
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
import org.springframework.core.io.Resource;
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
    RestTemplate restTemplate;

    public void init() throws Exception {
        connection = getConnection(ConfigDefault.APPLICATION_CONFIG_FILE);
        restTemplate = getRestTemplate(connection);

        logger.info(connection.toString());
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
        logger.info("request: {}; url: {}", request.toString(), url);

        checkRequest(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        ResponseEntity<T> httpResponse =
                restTemplate.exchange(url, HttpMethod.POST, httpRequest, responseType);

        checkResponse(httpResponse);
        T response = httpResponse.getBody();
        logger.info("response: {}", response.toString());

        return response;
    }

    private Connection getConnection(String config) throws WeCrossSDKException {
        Toml toml = ConfigUtils.getToml(config);
        Connection connection = new Connection();
        connection.setServer(getServer(toml));
        connection.setSSLKey(getSSLKey(toml));
        connection.setSSLCert(getSSLCert(toml));
        connection.setSSLCert(getCACert(toml));
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

    private String getSSLKey(Toml toml) throws WeCrossSDKException {
        String sslKey = toml.getString("connection.sslKey");
        if (sslKey == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.keyStore], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return sslKey;
    }

    private String getSSLCert(Toml toml) throws WeCrossSDKException {
        String sslCert = toml.getString("connection.sslCert");
        if (sslCert == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.keyStore], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return sslCert;
    }

    private String getCACert(Toml toml) throws WeCrossSDKException {
        String caCert = toml.getString("connection.caCert");
        if (caCert == null) {
            String errorMessage =
                    "Something wrong with parsing [connection.trustStore], please check configuration";
            throw new WeCrossSDKException(ErrorCode.FIELD_MISSING, errorMessage);
        }
        return caCert;
    }

    private RestTemplate getRestTemplate(Connection connection) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                    new PathMatchingResourcePatternResolver();
            Resource sslKey =
                    pathMatchingResourcePatternResolver.getResource(connection.getSSLKey());
            Resource sslCert =
                    pathMatchingResourcePatternResolver.getResource(connection.getSSLCert());
            Resource caCert =
                    pathMatchingResourcePatternResolver.getResource(connection.getCaCert());

            KeyCertLoader keyCertLoader = new KeyCertLoader();

            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(null);

            PrivateKey privateKey = keyCertLoader.toPrivateKey(sslKey.getInputStream(), null);
            X509Certificate[] certificates =
                    keyCertLoader.toX509Certificates(sslCert.getInputStream());
            keystore.setKeyEntry("mykey", privateKey, "".toCharArray(), certificates);

            KeyManagerFactory factory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            factory.init(keystore, "".toCharArray());
            KeyManager[] keyManagers = factory.getKeyManagers();

            KeyStore truststore = KeyStore.getInstance("jks");
            truststore.load(null);

            X509Certificate[] caCertificates =
                    keyCertLoader.toX509Certificates(caCert.getInputStream());
            truststore.setCertificateEntry("mykey", caCertificates[0]);

            TrustManagerFactory trustFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(truststore);
            TrustManager[] trustManagers = trustFactory.getTrustManagers();

            SSLContext context = SSLContext.getInstance(ConfigDefault.SSL_TYPE);
            context.init(keyManagers, trustManagers, SecureRandom.getInstanceStrong());

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            return new RestTemplate(requestFactory);
        } catch (Exception e) {

        }

        return null;
    }
}
