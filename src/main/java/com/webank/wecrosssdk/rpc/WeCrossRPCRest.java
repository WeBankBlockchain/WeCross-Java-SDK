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
        String uri = "/resource/" + path.replace('.', '/') + "/status";
        return new RemoteCall<>(weCrossService, uri, Response.class, new Request<>());
    }

    @Override
    public RemoteCall<ResourceDetailResponse> detail(String path) {
        String uri = "/resource/" + path.replace('.', '/') + "/detail";
        return new RemoteCall<>(weCrossService, uri, ResourceDetailResponse.class, new Request<>());
    }

    @Override
    public RemoteCall<Response> test() {
        return new RemoteCall<>(weCrossService, "/sys/test", Response.class, new Request<>());
    }

    @Override
    public RemoteCall<StubResponse> supportedStubs() {
        return new RemoteCall<>(
                weCrossService, "/sys/supportedStubs", StubResponse.class, new Request<>());
    }

    @Override
    public RemoteCall<AccountResponse> listAccount() {
        return new RemoteCall<>(
                weCrossService, "/auth/listAccount", AccountResponse.class, new Request<>());
    }

    @Override
    public RemoteCall<ResourceResponse> listResources(Boolean ignoreRemote) {
        ResourceRequest resourceRequest = new ResourceRequest(ignoreRemote);
        Request<ResourceRequest> request = new Request<>(resourceRequest);
        return new RemoteCall<>(
                weCrossService, "/sys/listResources", ResourceResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(String path, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        String txID = TransactionContext.currentXATransactionID();
        if (txID != null) {
            transactionRequest.addOption(Constant.XA_TRANSACTION_ID_KEY, txID);
            logger.info(
                    "call: TransactionID exist, turn to callTransaction, TransactionID is{}", txID);
        }
        String uri = "/resource/" + path.replace('.', '/') + "/call";
        Request<TransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(weCrossService, uri, TransactionResponse.class, request);
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
        String xaTransactionID = TransactionContext.currentXATransactionID();
        if (xaTransactionID != null && TransactionContext.isPathInTransaction(path)) {
            transactionRequest.addOption(Constant.XA_TRANSACTION_ID_KEY, xaTransactionID);
            long xaTransactionSeq = TransactionContext.currentXATransactionSeq();
            transactionRequest.addOption(Constant.XA_TRANSACTION_SEQ_KEY, xaTransactionSeq);
            logger.info(
                    "invoke: TransactionID exist, turn to execTransaction, TransactionID is {}, Seq is{}",
                    xaTransactionID,
                    xaTransactionSeq);
        }
        return buildSendTransactionRequest(path, transactionRequest);
    }

    @Override
    public RemoteCall<TransactionResponse> callXA(
            String transactionID, String path, String method, String... args) {
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        transactionRequest.addOption(Constant.XA_TRANSACTION_ID_KEY, transactionID);

        String uri = "/resource/" + path.replace('.', '/') + "/call";
        Request<TransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(weCrossService, uri, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendXATransaction(
            String transactionID, String path, String method, String... args) {
        long xaTransactionSeq = TransactionContext.currentXATransactionSeq();
        TransactionRequest transactionRequest = new TransactionRequest(method, args);
        transactionRequest.addOption(Constant.XA_TRANSACTION_ID_KEY, transactionID);
        transactionRequest.addOption(Constant.XA_TRANSACTION_SEQ_KEY, xaTransactionSeq);

        return buildSendTransactionRequest(path, transactionRequest);
    }

    @Override
    public RemoteCall<XAResponse> startXATransaction(String transactionID, String[] paths) {
        XATransactionRequest XATransactionRequest = new XATransactionRequest(transactionID, paths);

        Request<XATransactionRequest> request = new Request<>(XATransactionRequest);
        return new RemoteCall<>(
                weCrossService, "/xa/startXATransaction", XAResponse.class, request);
    }

    @Override
    public RemoteCall<XAResponse> commitXATransaction(String transactionID, String[] paths) {
        XATransactionRequest XATransactionRequest = new XATransactionRequest(transactionID, paths);

        Request<XATransactionRequest> request = new Request<>(XATransactionRequest);
        return new RemoteCall<>(
                weCrossService, "/xa/commitXATransaction", XAResponse.class, request);
    }

    @Override
    public RemoteCall<XAResponse> rollbackXATransaction(String transactionID, String[] paths) {
        XATransactionRequest XATransactionRequest = new XATransactionRequest(transactionID, paths);
        Request<XATransactionRequest> request = new Request<>(XATransactionRequest);
        return new RemoteCall<>(
                weCrossService, "/xa/rollbackXATransaction", XAResponse.class, request);
    }

    @Override
    public RemoteCall<XATransactionResponse> getXATransaction(
            String transactionID, String[] paths) {
        XATransactionRequest XATransactionRequest = new XATransactionRequest(transactionID, paths);
        Request<XATransactionRequest> request = new Request<>(XATransactionRequest);
        return new RemoteCall<>(
                weCrossService, "/xa/getXATransaction", XATransactionResponse.class, request);
    }

    @Override
    public RemoteCall<CommandResponse> customCommand(String command, String path, Object... args) {
        CommandRequest commandRequest = new CommandRequest(command, args);
        Request<CommandRequest> request = new Request<>(commandRequest);
        String uri = "/resource/" + path.replace('.', '/') + "/customCommand";
        return new RemoteCall<>(weCrossService, uri, CommandResponse.class, request);
    }

    @Override
    public RemoteCall<XATransactionListResponse> listXATransactions(int size) {
        ListXATransactionsRequest listXATransactionsRequest = new ListXATransactionsRequest(size);
        Request<ListXATransactionsRequest> request = new Request<>(listXATransactionsRequest);
        return new RemoteCall<>(
                weCrossService, "/xa/listXATransactions", XATransactionListResponse.class, request);
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
        Request<UARequest> request = new Request<>(uaRequest);
        return new RemoteCall<>(weCrossService, "/auth/register", UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> login(String name, String password) {
        UARequest uaRequest = new UARequest(name, password);
        Request<UARequest> request = new Request<>(uaRequest);
        return new RemoteCall<>(weCrossService, "/auth/login", UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> logout() {
        UARequest uaRequest = new UARequest();
        Request<UARequest> request = new Request<>(uaRequest);
        return new RemoteCall<>(weCrossService, "/auth/logout", UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> addChainAccount(String type, ChainAccount chainAccount) {
        Request<ChainAccount> request = new Request<>(chainAccount);
        return new RemoteCall<>(weCrossService, "/auth/addChainAccount", UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> setDefaultAccount(String type, ChainAccount chainAccount) {
        Request<ChainAccount> request = new Request<>(chainAccount);
        return new RemoteCall<>(
                weCrossService, "/auth/setDefaultAccount", UAResponse.class, request);
    }

    @Override
    public RemoteCall<UAResponse> setDefaultAccount(String type, Integer keyID) {
        ChainAccount chainAccount = new ChainAccount(keyID, type, true);
        Request<ChainAccount> request = new Request<>(chainAccount);
        return new RemoteCall<>(
                weCrossService, "/auth/setDefaultAccount", UAResponse.class, request);
    }

    @Override
    public String getCurrentTransactionID() {
        String txID = TransactionContext.currentXATransactionID();
        if (txID == null) {
            logger.warn("getCurrentTransactionID: Current TransactionID is null.");
            return null;
        }
        return txID;
    }

    private RemoteCall<TransactionResponse> buildSendTransactionRequest(
            String path, TransactionRequest transactionRequest) {
        String uri = "/resource/" + path.replace('.', '/') + "/sendTransaction";
        Request<TransactionRequest> request = new Request<>(transactionRequest);
        return new RemoteCall<>(weCrossService, uri, TransactionResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
