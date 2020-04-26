package com.webank.wecrosssdk.rpc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.common.ConfigDefault;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.utils.ConfigUtils;
import com.webank.wecrosssdk.utils.KeyCertLoader;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.Future;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
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

    private String server;
    private RestTemplate restTemplate;
    private CloseableHttpAsyncClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void init() throws WeCrossSDKException {
        Connection connection = getConnection(ConfigDefault.APPLICATION_CONFIG_FILE);
        logger.info(connection.toString());
        System.setProperty("jdk.tls.namedGroups", "secp256k1");
        server = connection.getServer();
        restTemplate = getRestTemplate(connection);
        httpClient = getHttpAsyncClient(connection);
        httpClient.start();
    }

    private void checkRequest(Request<?> request) throws WeCrossSDKException {
        if (request.getVersion().isEmpty()) {
            throw new WeCrossSDKException(ErrorCode.RPC_ERROR, "Request version is empty");
        }
        if (request.getMethod().isEmpty()) {
            throw new WeCrossSDKException(ErrorCode.RPC_ERROR, "Request method is empty");
        }
    }

    private <T extends Response> void checkResponse(ResponseEntity<T> response)
            throws WeCrossSDKException {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "method not exists: " + response.toString());
        }
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType)
            throws WeCrossSDKException {
        String url = RPCUtils.pathToUrl(server, request.getPath()) + "/" + request.getMethod();
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

    @Override
    public Future<HttpResponse> asyncSend(Request<?> request, FutureCallback<HttpResponse> callback)
            throws WeCrossSDKException {
        try {
            String url = RPCUtils.pathToUrl(server, request.getPath()) + "/" + request.getMethod();
            logger.info("request: {}; url: {}", request.toString(), url);
            checkRequest(request);
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(request));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return httpClient.execute(httpPost, callback);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            logger.error("Encode json error when async sending: {}", e);
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR, "Encode json error when async sending: " + e);
        }
    }

    private Connection getConnection(String config) throws WeCrossSDKException {
        Toml toml = ConfigUtils.getToml(config);
        Connection connection = new Connection();
        connection.setServer(getServer(toml));
        connection.setMaxTotal(toml.getLong("connection.maxTotal", 200L).intValue());
        connection.setMaxPerRoute(toml.getLong("connection.maxPerRoute", 8L).intValue());
        connection.setSSLKey(getSSLKey(toml));
        connection.setSSLCert(getSSLCert(toml));
        connection.setCaCert(getCACert(toml));
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

    private SSLContext getSSLContext(Connection connection) throws WeCrossSDKException {
        try {
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                    new PathMatchingResourcePatternResolver();
            Resource sslKey =
                    pathMatchingResourcePatternResolver.getResource(connection.getSSLKey());
            Resource sslCert =
                    pathMatchingResourcePatternResolver.getResource(connection.getSSLCert());
            Resource caCert =
                    pathMatchingResourcePatternResolver.getResource(connection.getCaCert());

            KeyCertLoader keyCertLoader = new KeyCertLoader();

            KeyStore keystore = KeyStore.getInstance("pkcs12");
            keystore.load(null);

            PrivateKey privateKey = keyCertLoader.toPrivateKey(sslKey.getInputStream(), null);
            X509Certificate[] certificates =
                    keyCertLoader.toX509Certificates(sslCert.getInputStream());
            keystore.setKeyEntry("mykey", privateKey, "".toCharArray(), certificates);

            KeyManagerFactory factory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            factory.init(keystore, "".toCharArray());
            KeyManager[] keyManagers = factory.getKeyManagers();

            KeyStore truststore = KeyStore.getInstance("pkcs12");
            truststore.load(null);

            X509Certificate[] caCertificates =
                    keyCertLoader.toX509Certificates(caCert.getInputStream());
            truststore.setCertificateEntry("mykey", caCertificates[0]);

            TrustManagerFactory trustFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(truststore);
            TrustManager[] trustManagers = trustFactory.getTrustManagers();

            SSLContext context = SSLContext.getInstance(ConfigDefault.SSL_TYPE);
            context.init(keyManagers, trustManagers, new SecureRandom());
            return context;
        } catch (Exception e) {
            logger.error("Init SSLContext error: {}", e);
            throw new WeCrossSDKException(ErrorCode.INTERNAL_ERROR, "Init SSLContext error: " + e);
        }
    }

    private RestTemplate getRestTemplate(Connection connection) throws WeCrossSDKException {
        try {
            SSLConnectionSocketFactory csf =
                    new SSLConnectionSocketFactory(
                            getSSLContext(connection), NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", csf)
                            .build();
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolingHttpClientConnectionManager.setMaxTotal(connection.getMaxTotal());
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(connection.getMaxPerRoute());
            CloseableHttpClient httpClient =
                    HttpClients.custom()
                            .setConnectionManager(poolingHttpClientConnectionManager)
                            .build();
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            logger.error("Init rest template error: {}", e);
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR, "Init rest template error: " + e);
        }
    }

    private CloseableHttpAsyncClient getHttpAsyncClient(Connection connection)
            throws WeCrossSDKException {
        try {
            //            Registry<SchemeIOSessionStrategy> registry =
            //                    RegistryBuilder.<SchemeIOSessionStrategy>create()
            //                            .register("http", NoopIOSessionStrategy.INSTANCE)
            //                            .register("https", new
            // SSLIOSessionStrategy(getSSLContext(connection)))
            //                            .build();
            //            IOReactorConfig ioReactorConfig =
            //                    IOReactorConfig.custom()
            //
            // .setIoThreadCount(Runtime.getRuntime().availableProcessors())
            //                            .setSoKeepAlive(true)
            //                            .build();
            //            ConnectingIOReactor ioReactor;
            //            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            //            PoolingNHttpClientConnectionManager connManager =
            //                    new PoolingNHttpClientConnectionManager(ioReactor, registry);
            //            connManager.setMaxTotal(connection.getMaxTotal());
            //            connManager.setDefaultMaxPerRoute(connection.getMaxPerRoute());
            RequestConfig requestConfig =
                    RequestConfig.custom()
                            .setConnectTimeout(10000)
                            .setSocketTimeout(10000)
                            .setConnectionRequestTimeout(1000)
                            .build();
            CloseableHttpAsyncClient httpClient =
                    HttpAsyncClients.custom()
                            .setSSLContext(getSSLContext(connection))
                            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                            .setDefaultRequestConfig(requestConfig)
                            .setMaxConnTotal(connection.getMaxTotal())
                            .setMaxConnPerRoute(connection.getMaxPerRoute())
                            .build();
            return httpClient;
        } catch (Exception e) {
            logger.error("Init http client error: {}", e);
            throw new WeCrossSDKException(ErrorCode.INTERNAL_ERROR, "Init http client error: " + e);
        }
    }
}
