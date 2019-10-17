package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.methods.Request;
import com.webank.wecrosssdk.methods.Response;
import com.webank.wecrosssdk.utils.Path;
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

public class Service implements WeCrossRpcService {
    private Logger logger = LoggerFactory.getLogger(WeCrossRpcService.class);

    // specify wecross node ip and port
    private String server;

    public Service(String server) {
        this.server = server;
    }

    private void checkRequest(Request<?> request) throws Exception {
        if (request.getVersion().isEmpty()) {
            throw new Exception("request version is empty");
        }
        if (request.getPath().isEmpty()) {
            throw new Exception("request path is empty");
        }
        if (request.getMethod().isEmpty()) {
            throw new Exception("request method is empty");
        }
    }

    private <T extends Response> void checkResponse(ResponseEntity<T> response) throws Exception {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Method not exists: " + response.toString());
        }
    }

    @Override
    public String getWeCrossServer() {
        return server;
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) {
        String url = Path.pathToUrl(server, request.getPath()) + "/" + request.getMethod();
        logger.info("method: {}; url: {}", request.getMethod(), url);
        try {
            checkRequest(request);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
            ResponseEntity<T> httpResponse =
                    restTemplate.exchange(url, HttpMethod.POST, httpRequest, responseType);

            checkResponse(httpResponse);
            Response<T> response = httpResponse.getBody();
            logger.info(
                    "Receive status:{} message:{} data:{}",
                    response.getResult(),
                    response.getMessage(),
                    response.getData());

            if (response.getData() != null) {
                logger.info("response data: {}", response.getData());

                // handle return values
                return (T) response;
            }

        } catch (Exception e) {
            logger.error("send rpc request error", e);
            Response response = new Response(1, e.getMessage(), null);
            return (T) response;
        }
        return null;
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
