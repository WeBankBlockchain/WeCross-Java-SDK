package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.*;

public interface WeCrossRPC {

    RemoteCall<Response> test();

    RemoteCall<StubResponse> supportedStubs();

    RemoteCall<AccountResponse> listAccount();

    RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote);

    RemoteCall<Response> status(String path);

    RemoteCall<ResourceDetailResponse> detail(String path);

    RemoteCall<TransactionResponse> call(String path, String method, String... args);

    RemoteCall<TransactionResponse> sendTransaction(String path, String method, String... args);

    RemoteCall<TransactionResponse> invoke(String path, String method, String... args)
            throws WeCrossSDKException;

    RemoteCall<TransactionResponse> callXA(
            String transactionID, String path, String method, String... args);

    RemoteCall<TransactionResponse> sendXATransaction(
            String transactionID, String path, String method, String... args);

    RemoteCall<XAResponse> startXATransaction(String transactionID, String[] paths);

    RemoteCall<XAResponse> commitXATransaction(String transactionID, String[] paths);

    RemoteCall<XAResponse> rollbackXATransaction(String transactionID, String[] paths);

    RemoteCall<XATransactionResponse> getXATransaction(String transactionID, String[] paths);

    RemoteCall<CommandResponse> customCommand(String command, String path, Object... args);

    RemoteCall<XATransactionListResponse> listXATransactions(int size);

    RemoteCall<UAResponse> register(String name, String password) throws WeCrossSDKException;

    RemoteCall<UAResponse> login(String name, String password);

    RemoteCall<UAResponse> logout();

    RemoteCall<UAResponse> addChainAccount(String type, ChainAccount chainAccount);

    RemoteCall<UAResponse> setDefaultAccount(String type, ChainAccount chainAccount);

    RemoteCall<UAResponse> setDefaultAccount(String type, Integer keyID);

    String getCurrentTransactionID();
}
