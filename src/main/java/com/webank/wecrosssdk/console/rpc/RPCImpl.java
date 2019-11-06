package com.webank.wecrosssdk.console.rpc;

import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.console.common.HelpInfo;
import com.webank.wecrosssdk.console.common.WeCrossServers;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.WeCrossService;
import com.webank.wecrosssdk.rpc.methods.Resources;
import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;
import com.webank.wecrosssdk.rpc.methods.WeCrossResource;
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
        if (params.length < 3) {
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
        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.call(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.call(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            return;
        }

        String result = transactionResponse.getCallContractResult().toString();
        ConsoleUtils.printJson(result);
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
        if (params.length < 3) {
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
        String method = ConsoleUtils.parseString(params[2]);

        TransactionResponse transactionResponse = null;
        if (params.length == 3) {
            transactionResponse = weCrossRPC.sendTransaction(path, method).send();
        } else {
            Object args[] = ConsoleUtils.parseArgs(params, 3);
            transactionResponse = weCrossRPC.sendTransaction(path, method, args).send();
        }

        if (transactionResponse.getResult() != 0) {
            System.out.println(transactionResponse.toString());
            return;
        }

        String result = transactionResponse.getCallContractResult().toString();
        ConsoleUtils.printJson(result);
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
