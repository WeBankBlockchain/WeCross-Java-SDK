package com.webank.wecrosssdk.rpc.common;

public class RegisterRequest {
    private String username;
    private String password;
    private String randomToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRandomToken() {
        return randomToken;
    }

    public void setRandomToken(String randomToken) {
        this.randomToken = randomToken;
    }

    @Override
    public String toString() {
        return "RegisterRequest{"
                + "username='"
                + username
                + '\''
                + ", password='"
                + password
                + '\''
                + ", randomToken='"
                + randomToken
                + '\''
                + '}';
    }
}
