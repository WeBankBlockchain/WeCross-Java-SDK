package com.webank.wecrosssdk.common;

public class Constant {
    public static final String APPLICATION_CONFIG_FILE = "classpath:application.toml";
    public static final String TEMPLATE_URL = "http://127.0.0.1:8250/";
    public static final String XA_TRANSACTION_ID_KEY = "XA_TRANSACTION_ID";
    public static final String XA_TRANSACTION_SEQ_KEY = "XA_TRANSACTION_SEQ";
    /** alphabet(upper or lower case) + digit + character("_-") ，length in (4,16) */
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{4,16}$";

    /** alphabet(upper or lower case) + digit + character("@+!%*#?") ，length in (4,16) */
    public static final String PASSWORD_PATTERN = "^[A-Za-z0-9@+!%*#?]{1,16}$";
}
