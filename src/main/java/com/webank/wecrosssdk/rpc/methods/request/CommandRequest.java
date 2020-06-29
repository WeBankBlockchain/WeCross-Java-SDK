package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class CommandRequest {
    private String command;
    private String path;
    private Object[] args;

    public CommandRequest(String command, String path, Object[] args) {
        this.command = command;
        this.path = path;
        this.args = args;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "CommandRequest{"
                + "command='"
                + command
                + '\''
                + ", path='"
                + path
                + '\''
                + ", args="
                + Arrays.toString(args)
                + '}';
    }
}
