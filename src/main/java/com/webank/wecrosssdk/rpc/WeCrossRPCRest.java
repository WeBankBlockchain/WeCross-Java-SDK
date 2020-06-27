package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.CommandRequest;
import com.webank.wecrosssdk.rpc.methods.request.ResourceRequest;
import com.webank.wecrosssdk.rpc.methods.request.RoutineRequest;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.*;
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
    public RemoteCall<ResourceDetailResponse> detail(String path) {
        Request<?> request = new Request(path, "", "detail", null);
        return new RemoteCall<ResourceDetailResponse>(
                weCrossService, ResourceDetailResponse.class, request);
    }

    @Override
    public RemoteCall<Response> test() {
        @SuppressWarnings("unchecked")
        Request<String> request = new Request("", "", "test", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
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
    public RemoteCall<TransactionResponse> call(Request<TransactionRequest> request) {
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(
            String path, String account, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(path, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(Request<TransactionRequest> request) {
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String account, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(path, method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> callTransaction(
            String transactionID, String path, String account, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(path, method, args);
        transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, transactionID);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> execTransaction(
            String transactionID,
            String seq,
            String path,
            String account,
            String method,
            String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(path, method, args);
        transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, transactionID);
        transactionRequest.addOption(Constant.TRANSACTION_SEQ_KEY, seq);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> startTransaction(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request =
                new Request(null, null, "startTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> commitTransaction(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request =
                new Request(null, null, "commitTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> rollbackTransaction(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request =
                new Request(null, null, "rollbackTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineInfoResponse> getTransactionInfo(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request =
                new Request(null, null, "getTransactionInfo", routineRequest);

        return new RemoteCall<RoutineInfoResponse>(
                weCrossService, RoutineInfoResponse.class, request);
    }

    @Override
    public RemoteCall<CommandResponse> customCommand(
            String command, String path, String account, Object... args) {
        CommandRequest commandRequest = new CommandRequest(command, path, args);

        @SuppressWarnings("unchecked")
        Request<CommandRequest> request = new Request(path, null, "customCommand", commandRequest);

        return new RemoteCall<CommandResponse>(weCrossService, CommandResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
