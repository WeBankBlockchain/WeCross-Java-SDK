package com.webank.wecrosssdk.rpc.service;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class WeCrossRPCService implements WeCrossService {
    private Logger logger = LoggerFactory.getLogger(WeCrossService.class);

    // specify wecross node ip and port
    private String server;

    public WeCrossRPCService(String server) {
        this.server = server;
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
        String url = RPCUtils.pathToUrl(server, request.getPath()) + "/" + request.getMethod();
        logger.info("method: {}; url: {}", request.getMethod(), url);

        checkRequest(request);

        RestTemplate restTemplate = new RestTemplate();
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

    @Override
    public void sendOnly(Request request) {}

    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType) {
        return null;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
