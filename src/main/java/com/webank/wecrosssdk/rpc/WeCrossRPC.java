package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.AccountResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceDetailResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.StubResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;

public interface WeCrossRPC {

    RemoteCall<Response> test();

    RemoteCall<StubResponse> supportedStubs();

    RemoteCall<AccountResponse> listAccounts();

    RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote);

    RemoteCall<Response> status(String path);

    RemoteCall<ResourceDetailResponse> detail(String path);

    RemoteCall<TransactionResponse> call(Request<TransactionRequest> request);

    RemoteCall<TransactionResponse> call(
            String path, String accountName, String method, String... args);

    RemoteCall<TransactionResponse> sendTransaction(Request<TransactionRequest> request);

    RemoteCall<TransactionResponse> sendTransaction(
            String path, String accountName, String method, String... args);
}
