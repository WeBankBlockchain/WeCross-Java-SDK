package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.GetDataRequest;
import com.webank.wecrosssdk.rpc.methods.request.ProposalRequest;
import com.webank.wecrosssdk.rpc.methods.request.ResourcesRequest;
import com.webank.wecrosssdk.rpc.methods.request.SetDataRequest;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.ProposalResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.methods.response.WeCrossResourceResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class WeCrossRPCRest implements WeCrossRPC {

    private WeCrossService weCrossService;

    public WeCrossRPCRest(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }

    @Override
    public RemoteCall<Response> status(String path) {
        //    String prefix = weCrossService.getWeCrossServer();
        //    String url = RPCUtils.pathToUrl(prefix, path);
        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "status", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<WeCrossResourceResponse> info(String path) {
        Request<?> request = new Request(path, "info", null);
        return new RemoteCall<WeCrossResourceResponse>(
                weCrossService, WeCrossResourceResponse.class, request);
    }

    @Override
    public RemoteCall<ResourcesResponse> list(Boolean ignoreRemote) {
        ResourcesRequest resourcesRequest = new ResourcesRequest(ignoreRemote);

        @SuppressWarnings("unchecked")
        Request<ResourcesRequest> request = new Request("", "list", resourcesRequest);
        return new RemoteCall<ResourcesResponse>(weCrossService, ResourcesResponse.class, request);
    }

    @Override
    public RemoteCall<GetDataResponse> getData(String path, String key) {
        GetDataRequest getDataRequest = new GetDataRequest(key);

        @SuppressWarnings("unchecked")
        Request<GetDataRequest> request = new Request(path, "getData", getDataRequest);
        return new RemoteCall<GetDataResponse>(weCrossService, GetDataResponse.class, request);
    }

    @Override
    public RemoteCall<SetDataResponse> setData(String path, String key, String value) {
        SetDataRequest setDataRequest = new SetDataRequest(key, value);

        @SuppressWarnings("unchecked")
        Request<SetDataRequest> request = new Request(path, "setData", setDataRequest);
        return new RemoteCall<SetDataResponse>(weCrossService, SetDataResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(String path, String method, Object... args) {
        TransactionRequest transactionRequest = new TransactionRequest(null, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request = new Request(path, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(
            String path, String[] retTypes, String method, Object... args) {
        TransactionRequest transactionRequest =
                new TransactionRequest(null, retTypes, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request = new Request(path, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(
            String path,
            byte[] proposalBytes,
            byte[] sign,
            String[] retTypes,
            String method,
            Object... args) {
        TransactionRequest transactionRequest =
                new TransactionRequest(proposalBytes, sign, retTypes, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request = new Request(path, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> callInt(String path, String method, Object... args) {
        return call(path, new String[] {"Int"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> callIntArray(
            String path, String method, Object... args) {
        return call(path, new String[] {"IntArray"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> callString(String path, String method, Object... args) {
        return call(path, new String[] {"String"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> callStringArray(
            String path, String method, Object... args) {
        return call(path, new String[] {"StringArray"}, method, args);
    }

    @Override
    public RemoteCall<ProposalResponse> callProposal(String path, String method, Object... args) {
        ProposalRequest proposalRequest = new ProposalRequest(method, null, null, args);

        @SuppressWarnings("unchecked")
        Request<ProposalRequest> request = new Request(path, "callProposal", proposalRequest);
        return new RemoteCall<ProposalResponse>(weCrossService, ProposalResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String method, Object... args) {
        TransactionRequest transactionRequest = new TransactionRequest(null, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String[] retTypes, String method, Object... args) {
        TransactionRequest transactionRequest =
                new TransactionRequest(null, retTypes, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path,
            byte[] proposalBytes,
            byte[] sign,
            String[] retTypes,
            String method,
            Object... args) {
        TransactionRequest transactionRequest =
                new TransactionRequest(proposalBytes, sign, retTypes, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransactionInt(
            String path, String method, Object... args) {
        return sendTransaction(path, new String[] {"Int"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransactionIntArray(
            String path, String method, Object... args) {
        return sendTransaction(path, new String[] {"IntArray"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransactionString(
            String path, String method, Object... args) {
        return sendTransaction(path, new String[] {"String"}, method, args);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransactionStringArray(
            String path, String method, Object... args) {
        return sendTransaction(path, new String[] {"StringArray"}, method, args);
    }

    @Override
    public RemoteCall<ProposalResponse> sendTransactionProposal(
            String path, String method, byte[] extraData, byte[] sign, Object... args) {
        ProposalRequest proposalRequest = new ProposalRequest(method, extraData, sign, args);

        @SuppressWarnings("unchecked")
        Request<ProposalRequest> request =
                new Request(path, "sendTransactionProposal", proposalRequest);
        return new RemoteCall<ProposalResponse>(weCrossService, ProposalResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
