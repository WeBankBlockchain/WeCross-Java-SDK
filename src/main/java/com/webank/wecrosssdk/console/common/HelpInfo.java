package com.webank.wecrosssdk.console.common;

public class HelpInfo {

    public static void promptHelp(String command) {
        System.out.println("Try '" + command + " -h or --help' for more information.");
        System.out.println();
    }

    public static boolean promptNoParams(String[] params, String funcName) {
        if (params.length == 2) {
            if ("-h".equals(params[1]) || "--help".equals(params[1])) {
                helpNoParams(funcName);
                return true;
            } else {
                promptHelp(funcName);
                return true;
            }
        } else if (params.length > 2) {
            promptHelp(funcName);
            return true;
        } else {
            return false;
        }
    }

    public static void helpNoParams(String func) {
        switch (func) {
            case "help":
            case "h":
                help();
                break;
            case "currentServer":
                currentServerHelp();
                break;
            case "listServers":
                listServersHelp();
                break;
            case "quit":
            case "q":
                quitHelp();
                break;

            default:
                break;
        }
    }

    public static void help() {
        System.out.println("Provide help information.");
        System.out.println("Usage: help");
        System.out.println();
    }

    public static void currentServerHelp() {
        System.out.println("Show currently connected WeCross server..");
        System.out.println("Usage: currentServer");
        System.out.println();
    }

    public static void listServersHelp() {
        System.out.println("List all configured WeCross servers.");
        System.out.println("Usage: listServers");
        System.out.println();
    }

    public static void switchHelp() {
        System.out.println("Switch to a specific WeCross server.");
        System.out.println("Usage: switch [server]");
        System.out.println("server -- The key of a server in your configuration.");
        System.out.println();
    }

    public static void listHelp() {
        System.out.println("List all resources configured by WeCross server.");
        System.out.println("Usage: list [option]");
        System.out.println("option -- 1: ignore remote source.");
        System.out.println("option -- 0: not ignore remote source.");
        System.out.println();
    }

    public static void existsHelp() {
        System.out.println("Check if the resource exists.");
        System.out.println("Usage: exists [path]");
        System.out.println("path: the path of resource in wecross server.");
        System.out.println();
    }

    public static void callHelp() {
        System.out.println("Call constant method of smart contract.");
        System.out.println("Usage:  call [path] [method] [...args]");
        System.out.println("path:   the path of the contract resource in wecross server.");
        System.out.println("method: the method in contract.");
        System.out.println("args:   variable parameter list.");
        System.out.println();
    }

    public static void sendTransactionHelp() {
        System.out.println("Call non-constant method of smart contract.");
        System.out.println("Usage:  sendTransaction [path] [method] [...args]");
        System.out.println("path:   the path of the contract resource in wecross server.");
        System.out.println("method: the method in contract.");
        System.out.println("args:   variable parameter list.");
        System.out.println();
    }

    public static void quitHelp() {
        System.out.println("Quit console.");
        System.out.println("Usage: quit or exit");
        System.out.println();
    }
}
