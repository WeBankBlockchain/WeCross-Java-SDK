package com.webank.wecrosssdk.utils;

import com.webank.wecrosssdk.exception.ConsoleException;
import com.webank.wecrosssdk.exception.Status;
import java.net.URL;

public class RPCUtils {

    public static void checkServer(String server) throws ConsoleException {
        String errorMessage = "Illegal ip:port: " + server;

        String ipUnits[] = server.split("\\.");
        if (ipUnits.length != 4) {
            throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
        }

        for (Integer i = 0; i < 3; ++i) {
            try {
                Integer ipUnit = Integer.parseInt(ipUnits[i]);
                if (ipUnit < 0 || ipUnit > 255) {
                    throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
                }
            } catch (NumberFormatException e) {
                throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
            }
        }

        String ipAndPort[] = ipUnits[3].split(":");
        if (ipAndPort.length != 2) {
            throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
        }

        try {
            Integer ipUnit = Integer.parseInt(ipAndPort[0]);
            if (ipUnit < 0 || ipUnit > 255) {
                throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
            }
        } catch (NumberFormatException e) {
            throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
        }

        try {
            Integer port = Integer.parseInt(ipAndPort[1]);
            if (port < 1 || port > 65535) {
                throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
            }
        } catch (NumberFormatException e) {
            throw new ConsoleException(Status.ILLEGAL_SERVER, errorMessage);
        }
    }

    public static String pathToUrl(String path) {
        return path.replace('.', '/');
    }

    public static String pathToUrl(String prefix, String path) {
        if (path.isEmpty()) {
            return "http://" + prefix;
        }
        return "http://" + prefix + "/" + path.replace('.', '/');
    }

    public static boolean isValidPath(String path) {
        String templateUrl = "http://127.0.0.1:8080/" + path.replace('.', '/');
        try {
            new URL(templateUrl);
        } catch (Exception e) {
            return false;
        }
        String unit[] = path.split("\\.");
        return (unit.length == 3);
    }
}
