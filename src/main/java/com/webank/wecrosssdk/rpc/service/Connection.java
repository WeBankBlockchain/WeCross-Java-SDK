package com.webank.wecrosssdk.rpc.service;

import static com.webank.wecrosssdk.common.Constant.SSL_ON_CLIENT_AUTH;

public class Connection {
    private String server;
    private String sslKey;
    private String sslCert;
    private String caCert;
    private int sslSwitch = SSL_ON_CLIENT_AUTH;
    private String urlPrefix;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSslKey() {
        return sslKey;
    }

    public void setSslKey(String sslKey) {
        this.sslKey = sslKey;
    }

    public String getSslCert() {
        return sslCert;
    }

    public void setSslCert(String sslCert) {
        this.sslCert = sslCert;
    }

    public String getCaCert() {
        return caCert;
    }

    public void setCaCert(String caCert) {
        this.caCert = caCert;
    }

    public int getSslSwitch() {
        return sslSwitch;
    }

    public void setSslSwitch(int sslSwitch) {
        this.sslSwitch = sslSwitch;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    @Override
    public String toString() {
        return "Connection{"
                + "server='"
                + server
                + '\''
                + ", sslKey='"
                + sslKey
                + '\''
                + ", sslCert='"
                + sslCert
                + '\''
                + ", caCert='"
                + caCert
                + '\''
                + ", sslSwitch="
                + sslSwitch
                + ", urlPrefix='"
                + urlPrefix
                + '\''
                + '}';
    }
}
