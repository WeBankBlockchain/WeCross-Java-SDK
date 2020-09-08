package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;
import java.util.List;

public class CommandList {
    /** doCommand Require JWT */
    public static final List<String> authRequiredCommands =
            Arrays.asList(
                    "call",
                    "sendTransaction",
                    "invoke",
                    "callTransaction",
                    "execTransaction",
                    "startTransaction",
                    "commitTransaction",
                    "rollbackTransaction",
                    "getTransactionInfo",
                    "logout",
                    "listAccounts",
                    "customCommand",
                    "addChainAccount",
                    "setDefaultAccount",
                    "listAccount");

    public static final List<String> normalCommands =
            Arrays.asList(
                    "register", "login", "supportedStubs", "listResources", "status", "detail");
}
