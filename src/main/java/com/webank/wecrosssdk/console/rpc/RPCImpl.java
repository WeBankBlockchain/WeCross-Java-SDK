package com.webank.wecrosssdk.console.rpc;

import com.webank.wecrosssdk.console.common.CallResult;
import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.console.common.HelpInfo;
import com.webank.wecrosssdk.console.common.WeCrossServers;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.data.CallContractResult;
import com.webank.wecrosssdk.rpc.data.Resources;
import com.webank.wecrosssdk.rpc.data.WeCrossResource;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCImpl implements RPCFace {

    private WeCrossServers weCrossServers;
    private Pair<String, String> currentServer;
    private WeCrossRPC weCrossRPC;

    private Logger logger = LoggerFactory.getLogger(RPCImpl.class);

    @Override
    public void setWeCrossServers(WeCrossServers weCrossServers) {
        this.weCrossServers = weCrossServers;
    }

    @Override
    public void setCurrentServer(Pair<String, String> currentServer) {
        this.currentServer = currentServer;
    }

    @Override
    public void setWeCrossRPC(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }

    @Override
    public Boolean switchServer(String[] params) {
        if (params.length != 2) {
            HelpInfo.promptHelp("switchServer");
            return false;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.switchHelp();
            return false;
        }

        String option = ConsoleUtils.parseString(params[1]);
        if (!weCrossServers.getServers().keySet().contains(option)) {
            System.out.println("Please provide a valid option");
            HelpInfo.switchHelp();
            return false;
        }

        String server = weCrossServers.getServers().get(option);
        currentServer = new Pair<>(option, server);
        WeCrossService weCrossService = new WeCrossRPCService(server);
        weCrossRPC = WeCrossRPC.init(weCrossService);
        return true;
    }

    @Override
    public void listResources(String[] params) throws Exception {
        if (params.length != 2) {
            HelpInfo.promptHelp("listResources");
            return;
        }
        String option = params[1];
        if ("-h".equals(option) || "--help".equals(option)) {
            HelpInfo.listHelp();
            return;
        }

        Boolean ignoreRemote = false;
        if (option.equals("1")) {
            ignoreRemote = true;
        } else if (option.equals("0")) {
            ignoreRemote = false;
        } else {
            System.out.println("Please provide a valid option");
            HelpInfo.listHelp();
            return;
        }
        ResourcesResponse resourcesResponse = weCrossRPC.list(ignoreRemote).send();
        if (resourcesResponse.getResult() != 0) {
            System.out.println(resourcesResponse.toString());
            return;
        }
        ConsoleUtils.printJson(resourcesResponse.getResources().toString());
    }

    @Override
    public void existsResource(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length != 2) {
            HelpInfo.promptHelp("existsResource");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.existsHelp();
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.existsHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }
        Response response = weCrossRPC.exists(path).send();
        if (response.getResult() != 0) {
            System.out.println(response.toString());
            return;
        }
        System.out.println("Result ==> " + response.getData());
        System.out.println();
    }

    @Override
    public void getData(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("getData");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.getDataHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("getData");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.getDataHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }
        String key = ConsoleUtils.parseString(params[2]);

        GetDataResponse getDataResponse = null;

        getDataResponse = weCrossRPC.getData(path, key).send();

        if (getDataResponse.getResult() != 0) {
            System.out.println(getDataResponse.toString());
            System.out.println();
            return;
        }

        String result = getDataResponse.getStatusAndValue().toString();
        ConsoleUtils.printJson(result);
    }

    @Override
    public void setData(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("setData");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.setDataHelp();
            return;
        }
        if (params.length < 4) {
            HelpInfo.promptHelp("setData");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.setDataHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }
        String key = ConsoleUtils.parseString(params[2]);
        String value = ConsoleUtils.parseString(params[3]);

        SetDataResponse setDataResponse = null;

        setDataResponse = weCrossRPC.setData(path, key, value).send();

        if (setDataResponse.getResult() != 0) {
            System.out.println(setDataResponse.toString());
            System.out.println();
            return;
        }

        String result = setDataResponse.getStatus().toString();
        ConsoleUtils.printJson(result);
    }

    @Override
    public void call(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("call");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.callHelp();
            return;
        }
        if (params.length < 4) {
            HelpInfo.promptHelp("call");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.callHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String retTypes[] = ConsoleUtils.parseRetTypes(params[2]);
        if (retTypes[0].equals("Void")) {
            retTypes = null;
        }

        String method = ConsoleUtils.parseString(params[3]);

        TransactionResponse transactionResponse = null;
        if (params.length == 4) {
            transactionResponse = weCrossRPC.call(path, retTypes, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 4);
            transactionResponse = weCrossRPC.call(path, retTypes, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        String result = transactionResponse.getCallContractResult().toString();
        ConsoleUtils.printJson(result);
    }

    @Override
    public void callInt(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("callInt");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.callIntHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("callInt");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.callHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.callInt(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.callInt(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void callIntArray(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("callIntArray");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.callIntArrayHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("callIntArray");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.callHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.callIntArray(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.callIntArray(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void callString(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("callString");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.callStringHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("callString");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.callHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.callString(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.callString(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void callStringArray(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("callStringArray");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.callStringArrayHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("callStringArray");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.callHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.callStringArray(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.callStringArray(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void sendTransaction(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("sendTransaction");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.sendTransactionHelp();
            return;
        }
        if (params.length < 4) {
            HelpInfo.promptHelp("sendTransaction");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.sendTransactionHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String retTypes[] = ConsoleUtils.parseRetTypes(params[2]);
        if (retTypes[0].equals("Void")) {
            retTypes = null;
        }
        String method = ConsoleUtils.parseString(params[3]);

        TransactionResponse transactionResponse = null;
        if (params.length == 4) {
            transactionResponse = weCrossRPC.sendTransaction(path, retTypes, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 4);
            transactionResponse = weCrossRPC.sendTransaction(path, retTypes, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        String result = transactionResponse.getCallContractResult().toString();
        ConsoleUtils.printJson(result);
    }

    @Override
    public void sendTransactionInt(String[] params, Map<String, String> pathMaps) throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("sendTransactionInt");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.sendTransactionIntHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("sendTransactionInt");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.sendTransactionHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.sendTransactionInt(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.sendTransactionInt(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void sendTransactionIntArray(String[] params, Map<String, String> pathMaps)
            throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("sendTransactionIntArray");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.sendTransactionIntArrayHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("sendTransactionIntArray");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.sendTransactionHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.sendTransactionIntArray(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.sendTransactionIntArray(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void sendTransactionString(String[] params, Map<String, String> pathMaps)
            throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("sendTransactionString");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.sendTransactionStringHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("sendTransactionString");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.sendTransactionHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.sendTransactionString(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.sendTransactionString(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public void sendTransactionStringArray(String[] params, Map<String, String> pathMaps)
            throws Exception {
        if (params.length == 1) {
            HelpInfo.promptHelp("sendTransactionStringArray");
            return;
        }
        if ("-h".equals(params[1]) || "--help".equals(params[1])) {
            HelpInfo.sendTransactionStringArrayHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("sendTransactionStringArray");
            return;
        }

        String path = ConsoleUtils.parseString(params[1]);
        if (!RPCUtils.isValidPath(path)) {
            if (!ConsoleUtils.isValidPathVar(params[1], pathMaps)) {
                System.out.println("Please provide a valid path");
                HelpInfo.sendTransactionHelp();
                return;
            }
            path = pathMaps.get(params[1]);
        }

        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.sendTransactionStringArray(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.sendTransactionStringArray(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            System.out.println();
            return;
        }

        CallContractResult callContractResult = transactionResponse.getCallContractResult();
        CallResult callResult =
                new CallResult(
                        callContractResult.getErrorCode(), callContractResult.getErrorMessage());
        if (callContractResult.getErrorCode() == 0) {
            callResult.setResult(callContractResult.getResult()[0]);
        }
        ConsoleUtils.printJson(callResult.toString());
    }

    @Override
    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        if (weCrossRPC == null) {
            paths.add("");
            return paths;
        }

        try {
            ResourcesResponse resourcesResponse = weCrossRPC.list(false).send();
            Resources resources = resourcesResponse.getResources();
            for (WeCrossResource weCrossResource : resources.getResourceList()) {
                paths.add(weCrossResource.getPath());
            }
        } catch (Exception e) {
            paths.add("");
            logger.warn("Get paths failed when starting console: {}", e.getMessage());
        }
        return paths;
    }
}
