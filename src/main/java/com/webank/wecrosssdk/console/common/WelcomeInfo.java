package com.webank.wecrosssdk.console.common;

public class WelcomeInfo {

    public static void welcome() {
        ConsoleUtils.doubleLine();
        System.out.println("Welcome to WeCross console(" + ConsoleVersion.Version + ")!");
        System.out.println("Type 'help' or 'h' for help. Type 'quit' or 'q' to quit console.");
        System.out.println();
        ConsoleUtils.doubleLine();
    }

    public static void help(String[] params) {
        if (HelpInfo.promptNoParams(params, "help")) {
            return;
        }
        if (params.length > 2) {
            HelpInfo.promptHelp("help");
            return;
        }
        ConsoleUtils.singleLine();
        StringBuilder sb = new StringBuilder();
        sb.append("quit                               Quit console.\n");
        sb.append("currentServer                      Show currently connected WeCross server.\n");
        sb.append("listServers                        List all configured WeCross servers.\n");
        sb.append("switch                             Switch to a specific WeCross server.\n");
        sb.append(
                "listLocalResources                 List local resources configured by WeCross server.\n");
        sb.append(
                "listResources                      List all resources including remote resources.\n");
        sb.append("exists                             Check if the resource exists.\n");
        sb.append("getData                            Get data from contract.\n");
        sb.append("setData                            Set data for contract.\n");
        sb.append("call                               Call constant method of smart contract.\n");
        sb.append(
                "sendTransaction                    Call non-constant method of smart contract.\n");
        sb.append(
                "WeCross.getResource                Init resource by path, and assign it to a custom variable.\n");
        sb.append("[resource].exists                  Equal to command: exists [path].\n");
        sb.append("[resource].call                    Equal to command: call [path].\n");
        sb.append("[resource].sendTransaction         Equal to command: sendTransaction [path].\n");

        System.out.println(sb.toString());
        ConsoleUtils.singleLine();
        System.out.println();
    }
}
