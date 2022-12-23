package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;
import java.util.List;

public class CommandList {
    /** doCommand Require JWT */
    public static final List<String> AUTH_REQUIRED_COMMANDS =
            Arrays.asList(
                    "call",
                    "sendTransaction",
                    "invoke",
                    "callXA",
                    "sendXATransaction",
                    "startXATransaction",
                    "commitXATransaction",
                    "rollbackXATransaction",
                    "getXATransaction",
                    "listXATransactions",
                    "logout",
                    "listAccounts",
                    "customCommand",
                    "addChainAccount",
                    "setDefaultAccount",
                    "setDefaultChainAccount",
                    "listAccount",
                    "supportedStubs",
                    "listResources",
                    "status",
                    "detail");

    public static final List<String> normalCommands = Arrays.asList("register", "login");
}
