package com.webank.wecrosssdk.rpc.common;

public class Connection {
    String server;
    String sslKey;
    String sslCert;
    String caCert;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSSLKey() {
        return sslKey;
    }

    public void setSSLKey(String keyStore) {
        this.sslKey = keyStore;
    }

    public String getSSLCert() {
        return sslCert;
    }

    public void setSSLCert(String trustStore) {
        this.sslCert = trustStore;
    }

    public String getCaCert() {
        return caCert;
    }

    public void setCaCert(String caCert) {
        this.caCert = caCert;
    }

    @Override
    public String toString() {
        return "Connection{"
                + "server='"
                + server
                + '\''
                + '\''
                + ", keyStore='"
                + sslKey
                + '\''
                + '\''
                + ", trustStore='"
                + sslCert
                + '\''
                + '\''
                + '}';
    }
}
