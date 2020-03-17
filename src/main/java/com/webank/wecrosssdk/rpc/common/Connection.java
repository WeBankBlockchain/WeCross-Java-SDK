package com.webank.wecrosssdk.rpc.common;

public class Connection {
    String server;
    String keyStoreType;
    String keyStore;
    String keyStorePass;
    String trustStore;
    String trustStorePass;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }

    @Override
    public String toString() {
        return "Connection{"
                + "server='"
                + server
                + '\''
                + ", keyStoreType='"
                + keyStoreType
                + '\''
                + ", keyStore='"
                + keyStore
                + '\''
                + ", keyStorePass='"
                + keyStorePass
                + '\''
                + ", trustStore='"
                + trustStore
                + '\''
                + ", trustStorePass='"
                + trustStorePass
                + '\''
                + '}';
    }
}
