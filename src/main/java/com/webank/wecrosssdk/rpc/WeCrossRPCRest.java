package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.TransactionContext;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.*;
import com.webank.wecrosssdk.rpc.methods.request.UARequest;
import com.webank.wecrosssdk.rpc.methods.response.*;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeCrossRPCRest implements WeCrossRPC {

    private Logger logger = LoggerFactory.getLogger(WeCrossRPCRest.class);

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
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        String txID = TransactionContext.currentTXID();
        if (txID != null) {
            transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, txID);
            logger.info(
                    "call: TransactionID exist, turn to callTransaction, TransactionID is{}", txID);
        }
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
        TransactionRequest transactionRequest = new TransactionRequest(method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> invoke(
            String path, String account, String method, String... args) throws WeCrossSDKException {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        String txID = TransactionContext.currentTXID();
        if (txID != null && TransactionContext.isPathInTransaction(path)) {
            try {
                transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, txID);
                String seq = TransactionContext.currentSeq();
                transactionRequest.addOption(Constant.TRANSACTION_SEQ_KEY, seq);
                logger.info(
                        "sendTransaction: TransactionID exist, turn to execTransaction, TransactionID is {}, Seq is{}",
                        txID,
                        seq);
            } catch (WeCrossSDKException e) {
                logger.error("sendTransaction: can not get current Seq.");
                throw new WeCrossSDKException(
                        e.getErrorCode(),
                        "sendTransaction TransactionContext.currentSeq() can not get current Seq.");
            }
        }
        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, account, "sendTransaction", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> callTransaction(
            String transactionID, String path, String account, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
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
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
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
        TransactionContext.txThreadLocal.set(transactionID);
        TransactionContext.seqThreadLocal.set(new AtomicInteger(1));
        List<String> pathInTransaction = Arrays.asList(paths);
        TransactionContext.pathInTransactionThreadLocal.set(pathInTransaction);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request = new Request("", "", "startTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> commitTransaction(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);
        TransactionContext.txThreadLocal.remove();
        TransactionContext.seqThreadLocal.remove();
        TransactionContext.pathInTransactionThreadLocal.remove();

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request = new Request("", "", "commitTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> rollbackTransaction(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);
        TransactionContext.txThreadLocal.remove();
        TransactionContext.seqThreadLocal.remove();
        TransactionContext.pathInTransactionThreadLocal.remove();

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request =
                new Request("", "", "rollbackTransaction", routineRequest);

        return new RemoteCall<RoutineResponse>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineInfoResponse> getTransactionInfo(
            String transactionID, String[] accounts, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, accounts, paths);

        @SuppressWarnings("unchecked")
        Request<RoutineRequest> request = new Request("", "", "getTransactionInfo", routineRequest);

        return new RemoteCall<RoutineInfoResponse>(
                weCrossService, RoutineInfoResponse.class, request);
    }

    @Override
    public RemoteCall<CommandResponse> customCommand(
            String command, String path, String account, Object... args) {
        CommandRequest commandRequest = new CommandRequest(command, args);

        @SuppressWarnings("unchecked")
        Request<CommandRequest> request =
                new Request(path, account, "customCommand", commandRequest);

        return new RemoteCall<CommandResponse>(weCrossService, CommandResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineIDResponse> getTransactionIDs(
            String path, String account, int option) {
        RoutineIDRequest routineIDRequest = new RoutineIDRequest(path, account, option);

        @SuppressWarnings("unchecked")
        Request<RoutineIDRequest> request =
                new Request("", "", "getTransactionIDs", routineIDRequest);

        return new RemoteCall<RoutineIDResponse>(weCrossService, RoutineIDResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> register(String name, String password) {
        UARequest uaRequest = new UARequest(name, password);

        Request<UARequest> request = new Request<>("auth", "register", uaRequest);

        return new RemoteCall<UAResponse>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> login(String name, String password) {
        UARequest uaRequest = new UARequest(name, password);

        Request<UARequest> request = new Request<>("auth", "login", uaRequest);

        return new RemoteCall<UAResponse>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> logout(String name, String password) {
        UARequest uaRequest = new UARequest(name, password);

        Request<UARequest> request = new Request<>("auth", "logout", uaRequest);

        return new RemoteCall<UAResponse>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> addChainAccount(String type, ChainAccount chainAccount) {
        UARequest uaRequest = new UARequest(type, chainAccount);

        Request<UARequest> request = new Request<>("auth", "addChainAccount", uaRequest);

        return new RemoteCall<UAResponse>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> setDefaultAccount(String type, ChainAccount chainAccount) {
        UARequest uaRequest = new UARequest(type, chainAccount);

        Request<UARequest> request = new Request<>("auth", "setDefaultAccount", uaRequest);

        return new RemoteCall<UAResponse>(weCrossService, UAResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
