package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.account.Account;
import com.webank.wecrosssdk.rpc.RemoteCall;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.ProposalResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
    private Logger logger = LoggerFactory.getLogger(Resource.class);
    private String iPath;
    private WeCrossRPC weCrossRPC;
    private Account account;

    // Use given account to send transaction
    Resource(WeCrossRPC weCrossRPC, String iPath, Account account) throws Exception {
        this.weCrossRPC = weCrossRPC;
        this.iPath = iPath;
        this.account = account;
        check();
    }

    // Use router's account to send transaction
    Resource(WeCrossRPC weCrossRPC, String iPath) throws Exception {
        this.weCrossRPC = weCrossRPC;
        this.iPath = iPath;
        check();
    }

    public void load() throws Exception {
        String data = (String) mustOkRequest(weCrossRPC.status(iPath)).getData();
        if (!data.equals("exists")) {
            throw new Exception("Resource " + iPath + " not found in WeCross network!");
        }
    }

    public GetDataResponse getData(String key) throws Exception {
        return (GetDataResponse) mustOkRequest(weCrossRPC.getData(iPath, key));
    }

    public SetDataResponse setData(String key, String value) throws Exception {
        return (SetDataResponse) mustOkRequest(weCrossRPC.setData(iPath, key, value));
    }

    public TransactionResponse call(String retTypes[], String method, Object... args)
            throws Exception {
        if (account != null) {
            return callWithLocalAccount(retTypes, method, args);
        } else {
            return callWithRouterAccount(retTypes, method, args);
        }
    }

    public TransactionResponse sendTransaction(String retTypes[], String method, Object... args)
            throws Exception {
        if (account != null) {
            return sendTransactionWithLocalAccount(retTypes, method, args);
        } else {
            return sendTransactionWithRouterAccount(retTypes, method, args);
        }
    }

    private TransactionResponse callWithRouterAccount(
            String retTypes[], String method, Object... args) throws Exception {
        return (TransactionResponse) mustOkRequest(weCrossRPC.call(iPath, retTypes, method, args));
    }

    private TransactionResponse callWithLocalAccount(
            String retTypes[], String method, Object... args) throws Exception {
        ProposalResponse.Proposal proposal =
                (ProposalResponse.Proposal)
                        mustOkRequest(weCrossRPC.callProposal(iPath, method, args)).getData();

        byte[] proposalBytes = proposal.getProposalToSign();
        byte[] sign = account.sign(proposalBytes);

        return (TransactionResponse)
                mustOkRequest(weCrossRPC.call(iPath, proposalBytes, sign, retTypes, method, args));
    }

    private TransactionResponse sendTransactionWithRouterAccount(
            String retTypes[], String method, Object... args) throws Exception {
        return (TransactionResponse)
                mustOkRequest(weCrossRPC.sendTransaction(iPath, retTypes, method, args));
    }

    private TransactionResponse sendTransactionWithLocalAccount(
            String retTypes[], String method, Object... args) throws Exception {
        ProposalResponse.Proposal proposal =
                (ProposalResponse.Proposal)
                        mustOkRequest(weCrossRPC.sendTransactionProposal(iPath, method, args))
                                .getData();

        byte[] proposalBytes = proposal.getProposalToSign();
        byte[] sign = account.sign(proposalBytes);

        return (TransactionResponse)
                mustOkRequest(
                        weCrossRPC.sendTransaction(
                                iPath, proposalBytes, sign, retTypes, method, args));
    }

    private void check() throws Exception {
        checkWeCrossRPC(this.weCrossRPC);
        checkIPath(this.iPath);
        checkAccount(this.account);
    }

    private void checkWeCrossRPC(WeCrossRPC weCrossRPC) throws Exception {
        if (weCrossRPC == null) {
            throw new Exception("WeCrossRPC not set");
        }
    }

    private void checkIPath(String iPath) throws Exception {
        String[] sp = iPath.split("\\.");
        if (sp.length < 3) {
            throw new Exception("Invalid IPath format: " + iPath);
        }
    }

    private void checkAccount(Account account) throws Exception {
        if (account != null) {
            // if has account
            try {
                byte[] msg = {0, 1, 2, 3};
                account.sign(msg);
            } catch (Exception e) {
                throw new Exception("Invalid account: " + account.getName());
            }
        }
    }

    private void checkResponse(Response response) throws Exception {
        if (response == null) {
            throw new Exception("Response null");
        } else if (response.getResult() != 0) {
            throw new Exception(
                    "Response error, errorCode: "
                            + response.getResult()
                            + " message: "
                            + response.getMessage());
        }
    }

    // If ok, get response. If error throw exception
    private Response mustOkRequest(RemoteCall call) throws Exception {
        Response response = call.send();
        checkResponse(response);
        return response;
    }
}
