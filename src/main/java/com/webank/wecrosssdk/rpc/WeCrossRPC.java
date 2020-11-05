package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.*;

public interface WeCrossRPC {

    RemoteCall<Response> test();

    RemoteCall<StubResponse> supportedStubs();

    RemoteCall<AccountResponse> listAccount();

    RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote);

    RemoteCall<Response> status(String path);

    RemoteCall<ResourceDetailResponse> detail(String path);

    RemoteCall<TransactionResponse> call(Request<TransactionRequest> request);

    RemoteCall<TransactionResponse> call(String path, String method, String... args);

    RemoteCall<TransactionResponse> sendTransaction(Request<TransactionRequest> request);

    RemoteCall<TransactionResponse> sendTransaction(String path, String method, String... args);

    RemoteCall<TransactionResponse> invoke(String path, String method, String... args)
            throws WeCrossSDKException;

    RemoteCall<TransactionResponse> callTransaction(
            String transactionID, String path, String method, String... args);

    RemoteCall<TransactionResponse> execTransaction(
            String transactionID, String path, String method, String... args);

    RemoteCall<RoutineResponse> startTransaction(String transactionID, String[] paths);

    RemoteCall<RoutineResponse> commitTransaction(String transactionID, String[] paths);

    RemoteCall<RoutineResponse> rollbackTransaction(String transactionID, String[] paths);

    RemoteCall<RoutineInfoResponse> getTransactionInfo(String transactionID, String[] paths);

    RemoteCall<CommandResponse> customCommand(String command, String path, Object... args);

    RemoteCall<RoutineIDResponse> getTransactionIDs(String path, int option);

    RemoteCall<UAResponse> register(String name, String password) throws WeCrossSDKException;

    RemoteCall<UAResponse> login(String name, String password);

    RemoteCall<UAResponse> logout();

    RemoteCall<UAResponse> addChainAccount(String type, ChainAccount chainAccount);

    RemoteCall<UAResponse> setDefaultAccount(String type, ChainAccount chainAccount);

    RemoteCall<UAResponse> setDefaultAccount(String type, Integer keyID);

    String getCurrentTransactionID();
}
