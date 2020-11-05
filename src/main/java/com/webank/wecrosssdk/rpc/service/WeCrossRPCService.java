package com.webank.wecrosssdk.rpc.service;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.UriDecoder;
import com.webank.wecrosssdk.rpc.common.CommandList;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.UARequest;
import com.webank.wecrosssdk.rpc.methods.response.UAResponse;
import com.webank.wecrosssdk.utils.ConfigUtils;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class WeCrossRPCService implements WeCrossService {
    private final Logger logger = LoggerFactory.getLogger(WeCrossRPCService.class);

    private String server;
    private AsyncHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int HTTP_CLIENT_TIME_OUT = 100000; // ms

    @Override
    public void init() throws WeCrossSDKException {
        Connection connection = getConnection(Constant.APPLICATION_CONFIG_FILE);
        logger.info("RPCService init:{}", connection);
        System.setProperty("jdk.tls.namedGroups", "secp256k1");
        server = connection.getServer();
        httpClient = getHttpAsyncClient(connection);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void checkRequest(Request<?> request) throws WeCrossSDKException {
        if (request.getVersion().isEmpty()) {
            throw new WeCrossSDKException(ErrorCode.RPC_ERROR, "Request version is empty");
        }
    }

    @Override
    public <T extends Response> T send(String uri, Request request, Class<T> responseType)
            throws WeCrossSDKException {
        checkRequest(request);
        CompletableFuture<T> responseFuture = new CompletableFuture<>();
        CompletableFuture<WeCrossSDKException> exceptionFuture = new CompletableFuture<>();

        asyncSend(
                uri,
                request,
                responseType,
                new Callback<T>() {
                    @Override
                    public void onSuccess(T response) {
                        responseFuture.complete(response);
                        exceptionFuture.complete(null);
                    }

                    @Override
                    public void onFailed(WeCrossSDKException e) {
                        logger.warn("send onFailed: ", e);
                        responseFuture.complete(null);
                        exceptionFuture.complete(e);
                    }
                });

        try {
            T response = responseFuture.get(20, TimeUnit.SECONDS);
            WeCrossSDKException exception = exceptionFuture.get(20, TimeUnit.SECONDS);

            if (logger.isDebugEnabled()) {
                logger.debug("response: {}", response);
            }

            if (exception != null) {
                throw exception;
            }

            if (response instanceof UAResponse) {
                getUAResponseInfo(uri, request, (UAResponse) response);
            }
            return response;
        } catch (TimeoutException e) {
            logger.warn("http request timeout");
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "http request timeout, caused by: " + e.getMessage());
        } catch (Exception e) {
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "http request failed, caused by: " + e.getMessage());
        }
    }

    public void getUAResponseInfo(String uri, Request request, UAResponse response)
            throws WeCrossSDKException {
        String query = uri.substring(1).split("/")[1];
        if ("login".equals(query)) {
            UARequest uaRequest = (UARequest) request.getData();
            String credential = response.getUAReceipt().getCredential();

            logger.info("CurrentUser: {}", uaRequest.getUsername());
            if (credential == null) {
                logger.error("Login fail, credential in UAResponse is null");
                throw new WeCrossSDKException(
                        ErrorCode.RPC_ERROR, "Login fail, credential in UAResponse is null!");
            }
            int firstIndexOfSpace = credential.trim().indexOf(' ');

            if (firstIndexOfSpace != -1) {
                String authenticationType = credential.substring(0, firstIndexOfSpace);
                AuthenticationManager.runtimeAuthType.set(authenticationType);
                credential = credential.substring(firstIndexOfSpace);
            }
            AuthenticationManager.setCurrentUser(uaRequest.getUsername(), credential);
        }
        if ("logout".equals(query)) {
            logger.info("CurrentUser: {} logout.", AuthenticationManager.getCurrentUser());
            AuthenticationManager.clearCurrentUser();
        }
    }

    @Override
    public <T extends Response> void asyncSend(
            String uri, Request<?> request, Class<T> responseType, Callback<T> callback) {
        try {
            String url = "https://" + server + uri;
            if (logger.isDebugEnabled()) {
                logger.debug("request: {}; url: {}", request, url);
            }

            checkRequest(request);
            BoundRequestBuilder builder = httpClient.preparePost(url);
            String currentUserCredential = AuthenticationManager.getCurrentUserCredential();

            UriDecoder uriDecoder = new UriDecoder(uri);
            String method = uriDecoder.getMethod();

            if (logger.isDebugEnabled()) {
                logger.debug("uri path: {}", method);
            }

            if (CommandList.AUTH_REQUIRED_COMMANDS.contains(method)) {
                if (currentUserCredential == null) {
                    logger.error("Request's method required AUTH, but current credential is null.");
                    throw new WeCrossSDKException(
                            ErrorCode.LACK_AUTHENTICATION,
                            "Command " + method + " needs Auth, please login.");
                }
                String runtimeAuthType = AuthenticationManager.runtimeAuthType.get();
                if (runtimeAuthType != null) {
                    currentUserCredential = runtimeAuthType + " " + currentUserCredential;
                }
                builder.setHeader(HttpHeaders.AUTHORIZATION, currentUserCredential);
            }

            builder.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .setBody(objectMapper.writeValueAsString(request))
                    .execute(
                            new AsyncCompletionHandler<Object>() {
                                @Override
                                public Object onCompleted(org.asynchttpclient.Response httpResponse)
                                        throws Exception {
                                    try {
                                        if (httpResponse.getStatusCode() == 401) {
                                            callback.callOnFailed(
                                                    new WeCrossSDKException(
                                                            ErrorCode.LACK_AUTHENTICATION,
                                                            "HTTP status code: 401-Unauthorized, have you logged in?\n"
                                                                    + "If you have logged-in already, maybe you should re-login "
                                                                    + "because your account login status has expired."));
                                            return null;
                                        }
                                        if (httpResponse.getStatusCode() != 200) {
                                            callback.callOnFailed(
                                                    new WeCrossSDKException(
                                                            ErrorCode.RPC_ERROR,
                                                            "HTTP response status: "
                                                                    + httpResponse.getStatusCode()
                                                                    + " message: "
                                                                    + httpResponse
                                                                            .getStatusText()));
                                            return null;
                                        } else {
                                            String content = httpResponse.getResponseBody();
                                            T response =
                                                    objectMapper.readValue(content, responseType);
                                            callback.callOnSuccess(response);
                                            return response;
                                        }
                                    } catch (Exception e) {
                                        callback.callOnFailed(
                                                new WeCrossSDKException(
                                                        ErrorCode.INTERNAL_ERROR,
                                                        "handle response failed: " + e.toString()));
                                        return null;
                                    }
                                }

                                @Override
                                public void onThrowable(Throwable t) {
                                    callback.callOnFailed(
                                            new WeCrossSDKException(
                                                    ErrorCode.RPC_ERROR,
                                                    "AsyncSend exception: "
                                                            + t.getCause().toString()));
                                }
                            });

        } catch (WeCrossSDKException e) {
            logger.error("Catch SDKException in asyncSend, errorMessage: {}", e.getMessage(), e);
            callback.callOnFailed(
                    new WeCrossSDKException(
                            ErrorCode.INTERNAL_ERROR,
                            "SDKException happened in asyncSend, errorMessage:" + e.getMessage()));
        } catch (Exception e) {
            logger.error("Encode json error when async sending: ", e);
            callback.callOnFailed(
                    new WeCrossSDKException(
                            ErrorCode.INTERNAL_ERROR,
                            "Encode json error when async sending: " + e.getMessage()));
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
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING,
                    "Something wrong with parsing [connection.server], please check configuration");
        }

        return server;
    }

    private String getSSLKey(Toml toml) throws WeCrossSDKException {
        String sslKey = toml.getString("connection.sslKey");
        if (sslKey == null) {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING,
                    "Something wrong with parsing [connection.keyStore], please check configuration");
        }
        return sslKey;
    }

    private String getSSLCert(Toml toml) throws WeCrossSDKException {
        String sslCert = toml.getString("connection.sslCert");
        if (sslCert == null) {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING,
                    "Something wrong with parsing [connection.keyStore], please check configuration");
        }
        return sslCert;
    }

    private String getCACert(Toml toml) throws WeCrossSDKException {
        String caCert = toml.getString("connection.caCert");
        if (caCert == null) {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING,
                    "Something wrong with parsing [connection.trustStore], please check configuration");
        }
        return caCert;
    }

    private SslContext getSslContext(Connection connection) throws IOException {

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        Resource sslKey = pathMatchingResourcePatternResolver.getResource(connection.getSSLKey());
        Resource sslCert = pathMatchingResourcePatternResolver.getResource(connection.getSSLCert());
        Resource caCert = pathMatchingResourcePatternResolver.getResource(connection.getCaCert());

        return SslContextBuilder.forClient()
                .trustManager(caCert.getInputStream())
                .keyManager(sslCert.getInputStream(), sslKey.getInputStream())
                .sslProvider(SslProvider.JDK)
                .clientAuth(ClientAuth.REQUIRE)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
    }

    private AsyncHttpClient getHttpAsyncClient(Connection connection) throws WeCrossSDKException {
        try {
            return asyncHttpClient(
                    config().setSslContext(getSslContext(connection))
                            .setConnectTimeout(HTTP_CLIENT_TIME_OUT)
                            .setRequestTimeout(HTTP_CLIENT_TIME_OUT)
                            .setReadTimeout(HTTP_CLIENT_TIME_OUT)
                            .setHandshakeTimeout(HTTP_CLIENT_TIME_OUT)
                            .setShutdownTimeout(HTTP_CLIENT_TIME_OUT)
                            .setSslSessionTimeout(HTTP_CLIENT_TIME_OUT)
                            .setPooledConnectionIdleTimeout(HTTP_CLIENT_TIME_OUT)
                            .setAcquireFreeChannelTimeout(HTTP_CLIENT_TIME_OUT)
                            .setConnectionPoolCleanerPeriod(HTTP_CLIENT_TIME_OUT)
                            // .setMaxConnections(connection.getMaxTotal())
                            // .setMaxConnectionsPerHost(connection.getMaxPerRoute())
                            .setKeepAlive(true));

        } catch (Exception e) {
            logger.error("Init http client error: ", e);
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR, "Init http client error: " + e.getMessage());
        }
    }
}
