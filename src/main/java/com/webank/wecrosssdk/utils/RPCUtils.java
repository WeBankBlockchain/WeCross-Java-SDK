package com.webank.wecrosssdk.utils;

import com.webank.wecrosssdk.common.Constant;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.net.URL;
import java.util.UUID;

public class RPCUtils {

    public static void checkPath(String path) throws WeCrossSDKException {
        String[] sp = path.split("\\.");
        if (!path.matches("^[A-Za-z]*.[A-Za-z0-9_-]*.[A-Za-z0-9_-]*$") || sp.length != 3) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_ERROR, "Invalid iPath: " + path);
        }
        String templateUrl = Constant.TEMPLATE_URL + path.replace('.', '/');

        try {
            new URL(templateUrl);
        } catch (Exception e) {
            throw new WeCrossSDKException(ErrorCode.ILLEGAL_SYMBOL, "Invalid iPath: " + path);
        }
    }

    public static String uriToUrl(String protocol, String prefix, String path) {
        if (path.isEmpty()) {
            return protocol + "://" + prefix;
        }
        return protocol + "://" + prefix + "/" + path.replace('.', '/');
    }

    public static String printHexDecently(String hexString) {
        if (hexString != null && hexString.length() > 10) {
            return hexString.substring(0, 10) + "...";
        } else {
            return hexString;
        }
    }

    public static String genTransactionID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
