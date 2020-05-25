package com.webank.wecrosssdk.rpc.service;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import java.util.concurrent.Future;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public interface WeCrossService {

    void init() throws WeCrossSDKException;

    <T extends Response> T send(Request request, Class<T> responseType) throws Exception;

    Future<HttpResponse> asyncSend(Request<?> request, FutureCallback<HttpResponse> callback)
            throws Exception;
    //    void sendOnly(Request request);
    //
    //    <T extends Response> CompletableFuture<T> sendAsync(Request request, Class<T>
    // responseType);
}
