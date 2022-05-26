package com.webank.wecrosssdk.mock;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.*;
import com.webank.wecrosssdk.rpc.common.account.BCOSAccount;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.common.account.FabricAccount;
import com.webank.wecrosssdk.rpc.common.account.UniversalAccount;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.*;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.ArrayList;
import java.util.List;

public class MockWeCrossService implements WeCrossService {

    @Override
    public void init() throws WeCrossSDKException {}

    @Override
    public <T extends Response> T send(
            String httpMethod, String uri, Request request, Class<T> responseType)
            throws Exception {
        int end = uri.contains("?") ? uri.indexOf("?") : uri.length();
        String[] splits = uri.substring(1, end).split("/");
        String method = splits[splits.length - 1];
        switch (method) {
            case "status":
                return (T) handleStatus(request);
            case "detail":
                return (T) handleDetail(request);
            case "supportedStubs":
                return (T) handleSupportedStubs(request);
            case "listAccount":
                return (T) handleListAccounts(request);
            case "listResources":
                return (T) handleListResources(request);
            case "call":
                return (T) handleCall(request);
            case "sendTransaction":
                return (T) handleSendTransaction(request);
            case "startXATransaction":
                return (T) handleStartTransaction(request);
            case "sendXATransaction":
                return (T) handleSendXATransaction(request);
            case "commitXATransaction":
                return (T) handleCommitTransaction(request);
            case "rollbackXATransaction":
                return (T) handleRollbackTransaction(request);
            case "getXATransaction":
                return (T) handleGetTransactionInfo(request);
            case "customCommand":
                return (T) handleCustomCommand(request);
            case "listXATransactions":
                return (T) handleGetTransactionIDs(request);
            case "register":
                return (T) handleRegister(request);
            case "login":
                return (T) handleLogin(request);
            case "addChainAccount":
                return (T) handleAddChainAccount(request);
            case "setDefaultAccount":
                return (T) handleSetDefaultAccount(request);
            case "setDefaultFabricAccount":
                return (T) handleSetDefaultFabricAccount(request);
            case "logout":
                return (T) handleLogout();
            default:
                return handleMethodNotFound(request, responseType);
        }
    }

    @Override
    public <T extends Response> void asyncSend(
            String httpMethod,
            String uri,
            Request<?> request,
            Class<T> responseType,
            Callback<T> callback) {
        return;
    }

    public Response handleStatus(Request request) {
        Response response = new Response();
        response.setErrorCode(0);
        response.setData("test");
        return response;
    }

    public ResourceDetailResponse handleDetail(Request request) {
        ResourceDetailResponse response = new ResourceDetailResponse();
        response.setErrorCode(0);
        response.setData(new ResourceDetail());
        return response;
    }

    public StubResponse handleSupportedStubs(Request request) {
        StubResponse response = new StubResponse();
        Stubs stubs = new Stubs();
        stubs.setStubTypes(new String[] {"BCOS", "Fabric"});
        response.setErrorCode(0);
        response.setStubs(stubs);
        return response; // Use Mockito to define handler
    }

    public AccountResponse handleListAccounts(Request request) {
        AccountResponse response = new AccountResponse();
        UniversalAccount account = new UniversalAccount("hello", "world");
        List<ChainAccount> list = new ArrayList<>();
        ChainAccount bcosAccount = new BCOSAccount(1, "BCOS2.0", "XXX", "XXX", "address", true);
        list.add(bcosAccount);
        ChainAccount fabricAccount =
                new FabricAccount(2, "Fabric1.4", true, "xxx", "xxx", "membershipID");
        ChainAccount fabricAccount2 =
                new FabricAccount(3, "Fabric2.0", true, "xxx", "xxx", "membershipID");
        list.add(fabricAccount);
        list.add(fabricAccount2);
        account.setChainAccounts(list);
        response.setErrorCode(0);
        response.setAccount(account);
        return response;
    }

    public ResourceResponse handleListResources(Request request) {
        ResourceResponse response = new ResourceResponse();
        Resources resources = new Resources();
        resources.setResourceDetails(new ResourceDetail[1]);
        response.setErrorCode(0);
        response.setResources(resources);
        return response;
    }

    public TransactionResponse handleCall(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response;
    }

    public TransactionResponse handleSendTransaction(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response;
    }

    public XAResponse handleStartTransaction(Request request) {
        XAResponse response = new XAResponse();
        response.setErrorCode(0);
        response.setXARawResponse(new RawXAResponse());
        return response;
    }

    public TransactionResponse handleSendXATransaction(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setResult(new String[] {"true"});
        response.setReceipt(receipt);
        return response;
    }

    public XAResponse handleCommitTransaction(Request request) {
        XAResponse response = new XAResponse();
        response.setErrorCode(0);
        response.setXARawResponse(new RawXAResponse());
        return response;
    }

    public XAResponse handleRollbackTransaction(Request request) {
        XAResponse response = new XAResponse();
        response.setErrorCode(0);
        response.setXARawResponse(new RawXAResponse());
        return response;
    }

    public XATransactionResponse handleGetTransactionInfo(Request request) {
        XATransactionResponse response = new XATransactionResponse();
        response.setErrorCode(0);
        response.setRawXATransactionResponse(new XATransactionResponse.RawXATransactionResponse());
        return response;
    }

    public CommandResponse handleCustomCommand(Request request) {
        CommandResponse response = new CommandResponse();
        response.setErrorCode(0);
        response.setResult("success");
        return response;
    }

    public XATransactionListResponse handleGetTransactionIDs(Request request) {
        XATransactionListResponse response = new XATransactionListResponse();
        response.setErrorCode(0);
        response.setRawXATransactionListResponse(
                new XATransactionListResponse.RawXATransactionListResponse());
        return response;
    }

    public UAResponse handleRegister(Request request) {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public UAResponse handleLogin(Request request) {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "", "token");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public UAResponse handleLogout() {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public UAResponse handleAddChainAccount(Request request) {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public UAResponse handleSetDefaultAccount(Request request) {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public UAResponse handleSetDefaultFabricAccount(Request request) {
        UAResponse response = new UAResponse();
        UAReceipt uaReceipt = new UAReceipt(0, "");
        response.setUAReceipt(uaReceipt);
        response.setErrorCode(0);
        return response;
    }

    public <T extends Response> T handleMethodNotFound(Request request, Class<T> responseType) {
        Response response = new Response();
        response.setErrorCode(-1);
        response.setMessage("Method not found");
        return (T) response;
    }
}
