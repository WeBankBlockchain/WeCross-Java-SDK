package com.webank.wecrosssdk.console.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WeCrossServers {
    private Map<String, String> servers = new HashMap<>();

    private String defaultServer;

    public Map<String, String> getServers() {
        return servers;
    }

    public Boolean areValidServers() {
        Pattern pattern = Pattern.compile("\\p{Lower}\\w*");
        for (String server : servers.keySet()) {
            if (!pattern.matcher(server).matches()) {
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
