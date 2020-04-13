package com.webank.wecrosssdk.exception;

public class ErrorCode {
    public static final int INTERNAL_ERROR = 100;

    // config
    public static final int FIELD_MISSING = 101;
    public static final int RESOURCE_ERROR = 102;
    public static final int ILLEGAL_SYMBOL = 103;

    // rpc
    public static final int REMOTECALL_ERROR = 201;
    public static final int RPC_ERROR = 202;
    public static final int CALL_CONTRACT_ERROR = 203;

    // performance
    public static final int RESOURCE_INACTIVE = 301;
    public static final int INVALID_CONTRACT = 302;
}
