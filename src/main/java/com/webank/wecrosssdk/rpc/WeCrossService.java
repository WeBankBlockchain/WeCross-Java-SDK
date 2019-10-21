package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import java.util.concurrent.CompletableFuture;

public interface WeCrossService {

    String getWeCrossServer();

    <T extends Response> T send(Request request, Class<T> responseType);

    void sendOnly(Request request);

    <T extends Response> CompletableFuture<T> sendAsync(Request request, Class<T> responseType);
}