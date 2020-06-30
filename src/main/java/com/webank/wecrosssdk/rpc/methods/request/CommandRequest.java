package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class CommandRequest {
    private String command;
    private Object[] args;

    public CommandRequest(String command, String path, Object[] args) {
        this.command = command;
        this.args = args;
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
                + ", args="
                + Arrays.toString(args)
                + '}';
    }
}
