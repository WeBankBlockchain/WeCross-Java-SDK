package com.webank.wecrosssdk.rpc.service;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.CommandList;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.UARequest;
import com.webank.wecrosssdk.rpc.methods.response.UAResponse;
import com.webank.wecrosssdk.utils.ConfigUtils;
import com.webank.wecrosssdk.utils.RPCUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class WeCrossRPCService implements WeCrossService {
    private Logger logger = LoggerFactory.getLogger(WeCrossRPCService.class);

    private String server;
    private AsyncHttpClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static int httpClientTimeOut = 100000; // ms

    @Override
    public void init() throws WeCrossSDKException {
        Connection connection = getConnection(Constant.APPLICATION_CONFIG_FILE);
        logger.info(connection.toString());
        System.setProperty("jdk.tls.namedGroups", "secp256k1");
        server = connection.getServer();
        httpClient = getHttpAsyncClient(connection);
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
        String url =
                RPCUtils.pathToUrl("https", server, request.getPath()) + "/" + request.getMethod();
        if (logger.isDebugEnabled()) {
            logger.debug("request: {}; url: {}", request.toString(), url);
        }

        checkRequest(request);
        CompletableFuture<T> responseFuture = new CompletableFuture<>();
        CompletableFuture<WeCrossSDKException> exceptionFuture = new CompletableFuture<>();

        asyncSend(
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
                        logger.warn("send onFailed: " + e.getMessage());
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
                if (request.getMethod().equals("login")) {
                    UARequest uaRequest = (UARequest) request.getData();
                    String token = ((UAResponse) response).getUAReceipt().getToken();

                    logger.info("CurrentUser: {}", uaRequest.getUsername());
                    if (token == null) {
                        logger.error("Token in UAResponse is null!");
                        throw new WeCrossSDKException(
                                ErrorCode.RPC_ERROR, "Login error: Token in UAResponse is null!");
                    }

                    AuthenticationManager.setCurrentUser(uaRequest.getUsername(), token);
                }
                if (request.getMethod().equals("logout")) {
                    logger.info("CurrentUser: {} logout.", AuthenticationManager.getCurrentUser());
                    AuthenticationManager.clearCurrentUser();
                }
            }
            return response;
        } catch (TimeoutException e) {
            logger.warn("http request timeout");
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "http request timeout, caused by: " + e.getMessage());
        } catch (Exception e) {
            logger.warn("send exception", e);
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "http request failed, caused by: " + e.getMessage());
        }
    }

    @Override
    public <T extends Response> void asyncSend(
            Request<?> request, Class<T> responseType, Callback<T> callback) {
        try {
            String url =
                    RPCUtils.pathToUrl("https", server, request.getPath())
                            + "/"
                            + request.getMethod();
            if (logger.isDebugEnabled()) {
                logger.debug("request: {}; url: {}", request.toString(), url);
            }

            checkRequest(request);
            BoundRequestBuilder builder = httpClient.preparePost(url);
            String currentToken = AuthenticationManager.getCurrentUserCredential();
            if (CommandList.authRequiredCommands.contains(request.getMethod())
                    && currentToken != null) {
                builder.setHeader(HttpHeaders.AUTHORIZATION, currentToken);
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
                                                            ErrorCode.RPC_ERROR,
                                                            "AsyncSend status: 401."
                                                                    + "Lack of authentication, please check current user's credential."));
                                            return null;
                                        }
                                        if (httpResponse.getStatusCode() != 200) {
                                            callback.callOnFailed(
                                                    new WeCrossSDKException(
                                                            ErrorCode.RPC_ERROR,
                                                            "AsyncSend status: "
                                                                    + httpResponse.getStatusCode()
                                                                    + " message: "
                                                                    + httpResponse
                                                                            .getStatusText()));
                                            return null;
                                        } else {
                                            String content = httpResponse.getResponseBody();
                                            T response =
                                                    (T)
                                                            objectMapper.readValue(
                                                                    content, responseType);
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

        } catch (Exception e) {
            logger.error("Encode json error when async sending: {}", e.getMessage());
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
                            .setConnectTimeout(httpClientTimeOut)
                            .setRequestTimeout(httpClientTimeOut)
                            .setReadTimeout(httpClientTimeOut)
                            .setHandshakeTimeout(httpClientTimeOut)
                            .setShutdownTimeout(httpClientTimeOut)
                            .setSslSessionTimeout(httpClientTimeOut)
                            .setPooledConnectionIdleTimeout(httpClientTimeOut)
                            .setAcquireFreeChannelTimeout(httpClientTimeOut)
                            .setConnectionPoolCleanerPeriod(httpClientTimeOut)
                            // .setMaxConnections(connection.getMaxTotal())
                            // .setMaxConnectionsPerHost(connection.getMaxPerRoute())
                            .setKeepAlive(true));

        } catch (Exception e) {
            logger.error("Init http client error: {}", e.getMessage());
            throw new WeCrossSDKException(ErrorCode.INTERNAL_ERROR, "Init http client error: " + e);
        }
    }
}
