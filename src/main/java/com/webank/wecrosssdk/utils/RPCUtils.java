package com.webank.wecrosssdk.utils;

public class RPCUtils {

    public static String pathToUrl(String path) {
        return path.replace('.', '/');
    }

    public static String pathToUrl(String prefix, String path) {
        if (path.isEmpty()) {
            return "https://" + prefix;
        }
        return "https://" + prefix + "/" + path.replace('.', '/');
    }
}
