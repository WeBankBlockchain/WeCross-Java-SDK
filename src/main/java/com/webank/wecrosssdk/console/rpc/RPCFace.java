package com.webank.wecrosssdk.console.rpc;

import com.webank.wecrosssdk.console.common.WeCrossServers;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

public interface RPCFace {
    void setWeCrossServers(WeCrossServers weCrossServers);

    void setCurrentServer(Pair<String, String> currentServer);

    void setWeCrossRPC(WeCrossRPC weCrossRPC);

    Boolean switchServer(String[] params);

    void listResources(String[] params) throws Exception;

    void existsResource(String[] params, Map<String, String> pathMaps) throws Exception;

    void call(String[] params, Map<String, String> pathMaps) throws Exception;

    void sendTransaction(String[] params, Map<String, String> pathMaps) throws Exception;

    List<String> getPaths();
}
