package com.webank.wecrosssdk.account;

public interface Account {
    byte[] sign(byte[] message) throws Exception;

    String getName();

    String getAddress();
}
