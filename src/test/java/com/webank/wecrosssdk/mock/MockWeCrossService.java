package com.webank.wecrosssdk.mock;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.GetDataRequest;
import com.webank.wecrosssdk.rpc.methods.request.SetDataRequest;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.ProposalResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.HashMap;
import java.util.Map;

public class MockWeCrossService implements WeCrossService {
    private Map<String, String> datas = new HashMap<>();

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws Exception {
        switch (request.getMethod()) {
            case "status":
                return (T) handleStatus(request);
            case "getData":
                return (T) handleGetData(request);
            case "setData":
                return (T) handleSetData(request);
            case "call":
                return (T) handleCall(request);
            case "callProposal":
                return (T) handleCallProposal(request);
            case "sendTransaction":
                return (T) handleSendTransaction(request);
            case "sendTransactionProposal":
                return (T) handleSendTransactionProposal(request);
            default:
                return handleMethodNotFound(request, responseType);
        }
    }

    public Response handleStatus(Request request) throws Exception {
        Response response = new Response();
        response.setResult(0);
        response.setData("exists");
        return response;
    }

    public GetDataResponse handleGetData(Request request) throws Exception {
        GetDataRequest req = (GetDataRequest) request.getData();
        if (datas.containsKey(req.getKey())) {

            String value = datas.get(req.getKey());

            GetDataResponse.StatusAndValue statusAndValue = new GetDataResponse.StatusAndValue();
            statusAndValue.setErrorCode(0);
            statusAndValue.setValue(value);
            GetDataResponse response = new GetDataResponse();
            response.setData(statusAndValue);
            response.setResult(0);

            return response;
        } else {
            GetDataResponse response = new GetDataResponse();
            response.setResult(-1);
            response.setMessage("not found key: " + req.getKey());

            return response;
        }
    }

    public SetDataResponse handleSetData(Request request) throws Exception {
        SetDataRequest req = (SetDataRequest) request.getData();
        datas.put(req.getKey(), req.getValue());

        SetDataResponse response = new SetDataResponse();
        response.setResult(0);

        SetDataResponse.Status status = new SetDataResponse.Status();
        status.setErrorCode(0);
        response.setStatus(status);

        return response;
    }

    public TransactionResponse handleCall(Request request) throws Exception {
        TransactionResponse response = new TransactionResponse();
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public ProposalResponse handleCallProposal(Request request) throws Exception {
        ProposalResponse.Proposal proposal = new ProposalResponse.Proposal();
        proposal.setErrorCode(0);
        proposal.setSeq(1);
        proposal.setProposalToSign(new byte[] {1, 2, 3, 4});
        ProposalResponse response = new ProposalResponse();
        response.setData(proposal);
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public TransactionResponse handleSendTransaction(Request request) throws Exception {
        TransactionResponse response = new TransactionResponse();
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public ProposalResponse handleSendTransactionProposal(Request request) throws Exception {
        ProposalResponse.Proposal proposal = new ProposalResponse.Proposal();
        proposal.setErrorCode(0);
        proposal.setSeq(1);
        proposal.setProposalToSign(new byte[] {1, 2, 3, 4});
        ProposalResponse response = new ProposalResponse();
        response.setData(proposal);
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public <T extends Response> T handleMethodNotFound(Request request, Class<T> responseType)
            throws Exception {
        Response response = new Response();
        response.setResult(-1);
        response.setMessage("Method not found");
        return (T) response;
    }
}
