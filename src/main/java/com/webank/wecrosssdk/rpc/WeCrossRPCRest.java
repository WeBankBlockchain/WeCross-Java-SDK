package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.TransactionContext;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.*;
import com.webank.wecrosssdk.rpc.methods.request.UARequest;
import com.webank.wecrosssdk.rpc.methods.response.*;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeCrossRPCRest implements WeCrossRPC {

    private final Logger logger = LoggerFactory.getLogger(WeCrossRPCRest.class);

    private WeCrossService weCrossService;

    public WeCrossRPCRest(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }

    @Override
    public RemoteCall<Response> status(String path) {
        Request<String> request = new Request<>(path, "status", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<ResourceDetailResponse> detail(String path) {
        Request<?> request = new Request<>(path, "detail", null);
        return new RemoteCall<>(weCrossService, ResourceDetailResponse.class, request);
    }

    @Override
    public RemoteCall<Response> test() {
        Request<String> request = new Request<>("", "test", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<StubResponse> supportedStubs() {
        Request<?> request = new Request<>("", "supportedStubs", null);
        return new RemoteCall<>(weCrossService, StubResponse.class, request);
    }

    @Override
    public RemoteCall<AccountResponse> listAccount() {
        Request<?> request = new Request<>("auth", "listAccount", null);
        return new RemoteCall<>(weCrossService, AccountResponse.class, request);
    }

    @Override
    public RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote) {
        ResourceRequest resourceRequest = new ResourceRequest(ignoreRemote);
        Request<ResourceRequest> request = new Request<>("", "listResources", resourceRequest);
        return new RemoteCall<>(weCrossService, ResourceResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(Request<TransactionRequest> request) {
        return new RemoteCall<>(weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(String path, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        String txID = TransactionContext.currentTXID();
        if (txID != null) {
            transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, txID);
            logger.info(
                    "call: TransactionID exist, turn to callTransaction, TransactionID is{}", txID);
        }
        Request<TransactionRequest> request = new Request<>(path, "call", transactionRequest);
        return new RemoteCall<>(weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(Request<TransactionRequest> request) {
        return new RemoteCall<>(weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        return buildSendTransactionRequest(path, transactionRequest);
    }

    @Override
    public RemoteCall<TransactionResponse> invoke(String path, String method, String... args)
            throws WeCrossSDKException {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        String txID = TransactionContext.currentTXID();
        if (txID != null && TransactionContext.isPathInTransaction(path)) {
            transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, txID);
            ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
            LocalDateTime localDateTime = LocalDateTime.now();
            String seq = String.valueOf(localDateTime.toEpochSecond(zoneOffset));
            transactionRequest.addOption(Constant.TRANSACTION_SEQ_KEY, seq);
            logger.info(
                    "invoke: TransactionID exist, turn to execTransaction, TransactionID is {}, Seq is{}",
                    txID,
                    seq);
        }
        return buildSendTransactionRequest(path, transactionRequest);
    }

    @Override
    public RemoteCall<TransactionResponse> callTransaction(
            String transactionID, String path, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, transactionID);

        Request<TransactionRequest> request = new Request<>(path, "call", transactionRequest);
        return new RemoteCall<>(weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> execTransaction(
            String transactionID, String path, String method, String... args) {
        // Chinese Standard Time UTC+8
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();
        String seq = String.valueOf(localDateTime.toEpochSecond(zoneOffset));
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        transactionRequest.addOption(Constant.TRANSACTION_ID_KEY, transactionID);
        transactionRequest.addOption(Constant.TRANSACTION_SEQ_KEY, seq);

        return buildSendTransactionRequest(path, transactionRequest);
    }

    @Override
    public RemoteCall<RoutineResponse> startTransaction(String transactionID, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, paths);

        Request<RoutineRequest> request = new Request<>("", "startTransaction", routineRequest);
        return new RemoteCall<>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> commitTransaction(String transactionID, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, paths);

        Request<RoutineRequest> request = new Request<>("", "commitTransaction", routineRequest);
        return new RemoteCall<>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineResponse> rollbackTransaction(String transactionID, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, paths);

        Request<RoutineRequest> request = new Request<>("", "rollbackTransaction", routineRequest);
        return new RemoteCall<>(weCrossService, RoutineResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineInfoResponse> getTransactionInfo(
            String transactionID, String[] paths) {
        RoutineRequest routineRequest = new RoutineRequest(transactionID, paths);

        Request<RoutineRequest> request = new Request<>("", "getTransactionInfo", routineRequest);

        return new RemoteCall<>(weCrossService, RoutineInfoResponse.class, request);
    }

    @Override
    public RemoteCall<CommandResponse> customCommand(String command, String path, Object... args) {
        CommandRequest commandRequest = new CommandRequest(command, args);

        Request<CommandRequest> request = new Request<>(path, "customCommand", commandRequest);

        return new RemoteCall<>(weCrossService, CommandResponse.class, request);
    }

    @Override
    public RemoteCall<RoutineIDResponse> getTransactionIDs(String path, int option) {
        RoutineIDRequest routineIDRequest = new RoutineIDRequest(path, option);

        Request<RoutineIDRequest> request =
                new Request<>("", "getTransactionIDs", routineIDRequest);

        return new RemoteCall<>(weCrossService, RoutineIDResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> register(String name, String password)
            throws WeCrossSDKException {
        UARequest uaRequest = new UARequest(name, password);
        if (!Pattern.matches(Constant.USERNAME_PATTERN, name)
                || !Pattern.matches(Constant.PASSWORD_PATTERN, password)) {
            throw new WeCrossSDKException(
                    ErrorCode.ILLEGAL_SYMBOL,
                    "Invalid username/password, please check your username/password matches the pattern.");
        }
        Request<UARequest> request = new Request<>("auth", "register", uaRequest);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> login(String name, String password) {
        UARequest uaRequest = new UARequest(name, password);

        Request<UARequest> request = new Request<>("auth", "login", uaRequest);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> logout() {
        UARequest uaRequest = new UARequest();

        Request<UARequest> request = new Request<>("auth", "logout", uaRequest);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> addChainAccount(String type, ChainAccount chainAccount) {

        Request<ChainAccount> request = new Request<>("auth", "addChainAccount", chainAccount);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> setDefaultAccount(String type, ChainAccount chainAccount) {

        Request<ChainAccount> request = new Request<>("auth", "setDefaultAccount", chainAccount);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> setDefaultAccount(String type, Integer keyID) {
        ChainAccount chainAccount = new ChainAccount(keyID, type, true);
        Request<ChainAccount> request = new Request<>("auth", "setDefaultAccount", chainAccount);

        return new RemoteCall<>(weCrossService, UAResponse.class, request);
    }

    @Override
    public String getCurrentTransactionID() {
        String txID = TransactionContext.currentTXID();
        if (txID == null) {
            logger.warn("getCurrentTransactionID: Current TransactionID is null.");
            return null;
        }
        return txID;
    }

    private RemoteCall<TransactionResponse> buildSendTransactionRequest(
            String path, TransactionRequest transactionRequest) {
        Request<TransactionRequest> request =
                new Request<>(path, "sendTransaction", transactionRequest);
        return new RemoteCall<>(weCrossService, TransactionResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
