package com.webank.wecrosssdk.exception;

public class Status {

    // common
    public static final int INTERNAL_ERROR = 1;

    // status in config
    public static final int NO_CONFIG_FOUND = 1000;
    public static final int UNEXPECTED_SERVER = 1001;
    public static final int ILLEGAL_SERVER = 1002;

    // status in param
    public static final int ILLEGAL_PARAM = 2000;
    public static final int METHOD_MISSING = 2000;
}
