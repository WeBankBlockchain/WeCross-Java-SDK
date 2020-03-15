package com.webank.wecrosssdk.config;

import java.io.File;

public class Default {
    public static final String APPLICATION_CONFIG_FILE = "classpath:application.toml";
    public static final String SSL_TYPE = "TLS";

    public static final String TEMPLATE_URL = "http://127.0.0.1:8080/";

    // Account
    public static final String ACCOUNTS_BASE = "classpath:accounts" + File.separator;
    public static final String ACCOUNTS_CONFIG_NAME = "accounts.toml";
    public static final String ACCOUNTS_CONFIG_FILE = ACCOUNTS_BASE + ACCOUNTS_CONFIG_NAME;
    public static final String ACCOUNT_CONFIG_NAME = "config.toml";
    /*
    public static final String MAIN_CONFIG_FILE = "classpath:wecross.toml";
    public static final String MAIN_CONFIG_TEST_FILE = "classpath:data/test.toml";
    public static final String STUB_CONFIG_FILE = "stub.toml";

    public static final Integer DEFAULT_TIME_OUT = 60 * 1000;
    public static final long DEFAULT_PROPOSAL_WAIT_TIME = 120000; // ms
    */
}
