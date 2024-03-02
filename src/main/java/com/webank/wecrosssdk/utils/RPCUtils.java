package com.webank.wecrosssdk.utils;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RPCUtils.class);

    public static void checkPath(String path) throws WeCrossSDKException {
        String[] sp = path.split("\\.");
        if (!path.matches("^[A-Za-z]*.[A-Za-z0-9_-]*.[A-Za-z0-9_-]*$") || sp.length != 3) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_ERROR, "Invalid iPath: " + path);
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

    public static String formatUrlPrefix(String urlPrefix) throws WeCrossSDKException {
        String pattern = "^/(?!_)(?!-)(?!.*?_$)(?!.*?-$)[\\w-]{1,18}$";
        String prefix = urlPrefix;
        if (prefix == null) {
            return null;
        }
        // /something/ => /something
        if (prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        // something => /something
        if (!prefix.startsWith("/")) {
            prefix = '/' + prefix;
        }
        // /something
        if (Pattern.matches(pattern, prefix)) {
            return prefix;
        } else {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING,
                    "Error 'urlPrefix' in config, it should matches pattern: " + pattern);
        }
    }

    public static String genTransactionID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
