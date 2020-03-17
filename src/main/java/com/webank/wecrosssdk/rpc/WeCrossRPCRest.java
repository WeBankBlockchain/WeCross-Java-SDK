package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.ResourceRequest;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.AccountResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceInfoResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.StubResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class WeCrossRPCRest implements WeCrossRPC {

    private WeCrossService weCrossService;

    public WeCrossRPCRest(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }

    @Override
    public RemoteCall<Response> status(String path) {
        @SuppressWarnings("unchecked")
        Request<String> request = new Request(path, "", "status", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<ResourceInfoResponse> info(String path) {
        Request<?> request = new Request(path, "", "info", null);
        return new RemoteCall<ResourceInfoResponse>(
                weCrossService, ResourceInfoResponse.class, request);
    }

    @Override
    public RemoteCall<StubResponse> supportedStubs() {
        @SuppressWarnings("unchecked")
        Request<?> request = new Request("", "", "supportedStubs", null);
        return new RemoteCall<StubResponse>(weCrossService, StubResponse.class, request);
    }

    @Override
    public RemoteCall<AccountResponse> listAccounts() {
        @SuppressWarnings("unchecked")
        Request<?> request = new Request("", "", "listAccounts", null);
        return new RemoteCall<AccountResponse>(weCrossService, AccountResponse.class, request);
    }

    @Override
    public RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote) {
        ResourceRequest resourceRequest = new ResourceRequest(ignoreRemote);

        @SuppressWarnings("unchecked")
        Request<ResourceRequest> request = new Request("", "", "listResources", resourceRequest);
        return new RemoteCall<ResourceResponse>(weCrossService, ResourceResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(
            String path, String accountName, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, accountName, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String accountName, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, accountName, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
