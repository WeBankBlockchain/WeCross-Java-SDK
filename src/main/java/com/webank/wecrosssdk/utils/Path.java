package com.webank.wecrosssdk.utils;

public class Path {

    public static String pathToUrl(String path) {
        return path.replace('.', '/');
    }

    public static String pathToUrl(String prefix, String path) {
        if (path.isEmpty()) {
            return "http://" + prefix;
        }
        return "http://" + prefix + "/" + path.replace('.', '/');
    }
}
