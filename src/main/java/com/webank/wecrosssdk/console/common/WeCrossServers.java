package com.webank.wecrosssdk.console.common;

import java.util.HashMap;
import java.util.Map;

public class WeCrossServers {
    private Map<String, String> servers = new HashMap<>();

    private String defaultServer;

    public Map<String, String> getServers() {
        return servers;
    }

    public Boolean areValidServers() {
        String regex = "^[a-z0-9A-Z]+$";
        for (String server : servers.keySet()) {
            if (!server.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public void setServers(Map<String, String> servers) {
        this.servers = servers;
    }

    public String getDefaultServer() {
        return defaultServer;
    }

    public void setDefaultServer(String defaultServer) {
        this.defaultServer = defaultServer;
    }
}
