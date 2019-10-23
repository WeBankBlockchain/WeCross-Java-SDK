package com.webank.wecrosssdk.integrationtest;

import com.webank.wecrosssdk.integrationtest.methods.Request;
import com.webank.wecrosssdk.integrationtest.methods.Response;
import java.util.concurrent.CompletableFuture;

public interface WeCrossService {

    String getWeCrossServer();

    <T extends Response> T send(Request request, Class<T> responseType);

    void sendOnly(Request request);

    <T extends Response> CompletableFuture<T> sendAsync(Request request, Class<T> responseType);
}
