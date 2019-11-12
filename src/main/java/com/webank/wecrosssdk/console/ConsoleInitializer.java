package com.webank.wecrosssdk.console;

import com.webank.wecrosssdk.console.common.WeCrossServers;
import com.webank.wecrosssdk.console.rpc.RPCFace;
import com.webank.wecrosssdk.console.rpc.RPCImpl;
import com.webank.wecrosssdk.exception.ConsoleException;
import com.webank.wecrosssdk.exception.Status;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.WeCrossService;
import com.webank.wecrosssdk.utils.RPCUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsoleInitializer {

    private ApplicationContext context;
    private WeCrossServers weCrossServers;
    private Pair<String, String> currentServer;
    private WeCrossRPC weCrossRPC;
    private RPCFace rpcFace;

    public void init(String args) throws ConsoleException {
        context = new ClassPathXmlApplicationContext("classpath:console.xml");
        WeCrossServers weCrossServers = context.getBean(WeCrossServers.class);

        if (weCrossServers == null || weCrossServers.getServers().size() == 0) {
            String errorMessage = "Error in WeCrossServers configuration";
            throw new ConsoleException(Status.NO_CONFIG_FOUND, errorMessage);
        }

        if (!weCrossServers.areValidServers()) {
            String errorMessage = "Illegal key found in configuration";
            throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
        }

        this.weCrossServers = weCrossServers;

        if (args.equals("")) {
            // default server
            String defaultServer = weCrossServers.getDefaultServer();
            if (!weCrossServers.getServers().keySet().contains(defaultServer)) {
                String errorMessage = "Error in defaultServer configuration";
                throw new ConsoleException(Status.UNEXPECTED_SERVER, errorMessage);
            }

            String server = weCrossServers.getServers().get(defaultServer);
            RPCUtils.checkServer(server);
            currentServer = new Pair<>(defaultServer, server);
            WeCrossService weCrossService = new WeCrossRPCService(server);
            weCrossRPC = WeCrossRPC.init(weCrossService);
        } else {
            // select by user
            if (!weCrossServers.getServers().keySet().contains(args)) {
                String errorMessage = "This server is not found in your configuration";
                throw new ConsoleException(Status.UNEXPECTED_SERVER, errorMessage);
            } else {
                String server = weCrossServers.getServers().get(args);
                RPCUtils.checkServer(server);
                currentServer = new Pair<>(args, server);
                WeCrossService weCrossService = new WeCrossRPCService(server);
                weCrossRPC = WeCrossRPC.init(weCrossService);
            }
        }

        rpcFace = new RPCImpl();
        rpcFace.setWeCrossRPC(weCrossRPC);
        rpcFace.setCurrentServer(currentServer);
        rpcFace.setWeCrossServers(weCrossServers);
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public WeCrossServers getWeCrossServers() {
        return weCrossServers;
    }

    public void setWeCrossServers(WeCrossServers weCrossServers) {
        this.weCrossServers = weCrossServers;
    }

    public Pair<String, String> getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(Pair<String, String> currentServer) {
        this.currentServer = currentServer;
    }

    public WeCrossRPC getWeCrossRPC() {
        return weCrossRPC;
    }

    public void setWeCrossRPC(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }

    public RPCFace getRpcFace() {
        return rpcFace;
    }

    public void setRpcFace(RPCFace rpcFace) {
        this.rpcFace = rpcFace;
    }
}
