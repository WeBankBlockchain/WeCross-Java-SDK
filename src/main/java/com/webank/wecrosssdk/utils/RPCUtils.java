package com.webank.wecrosssdk.utils;

import com.webank.wecrosssdk.common.ConfigDefault;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.net.URL;

public class RPCUtils {

    public static void checkPath(String path) throws WeCrossSDKException {
        String[] sp = path.split("\\.");
        if (sp.length < 3) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_ERROR, "Invalid iPath: " + path);
        }
        String templateUrl = ConfigDefault.TEMPLATE_URL + path.replace('.', '/');

        try {
            new URL(templateUrl);
        } catch (Exception e) {
            throw new WeCrossSDKException(ErrorCode.ILLEGAL_SYMBOL, "Invalid iPath: " + path);
        }
    }

    public static String pathToUrl(String prefix, String path) {
        if (path.isEmpty()) {
            return "https://" + prefix;
        }
        return "https://" + prefix + "/" + path.replace('.', '/');
    }
}
