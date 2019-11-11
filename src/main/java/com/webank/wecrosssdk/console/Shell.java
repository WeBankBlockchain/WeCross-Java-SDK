package com.webank.wecrosssdk.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.console.common.HelpInfo;
import com.webank.wecrosssdk.console.common.JlineUtils;
import com.webank.wecrosssdk.console.common.WelcomeInfo;
import com.webank.wecrosssdk.console.mock.MockWeCross;
import com.webank.wecrosssdk.console.rpc.RPCFace;
import com.webank.wecrosssdk.exception.ConsoleException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.Reference;

public class Shell {

    private static RPCFace rpcFace;

    public static void main(String[] args) {

        LineReader lineReader = null;
        GroovyShell groovyShell = null;
        MockWeCross mockWeCross = null;
        ObjectMapper mapper = null;
        Set<String> resourceVars = new HashSet<>();
        Set<String> pathVars = new HashSet<>();
        Map<String, String> pathMaps = new HashMap<>();
        Map<String, String> serverMaps = new HashMap<>();
        ConsoleInitializer consoleInitializer = new ConsoleInitializer();

        try {
            consoleInitializer.init("");
            rpcFace = consoleInitializer.getRpcFace();
            serverMaps = consoleInitializer.getWeCrossServers().getServers();
        } catch (ConsoleException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            Binding binding = new Binding();
            groovyShell = new GroovyShell(binding);
            mockWeCross = new MockWeCross(consoleInitializer.getWeCrossRPC());
            groovyShell.setProperty("WeCross", mockWeCross);

            lineReader =
                    JlineUtils.getLineReader(
                            rpcFace.getPaths(), resourceVars, pathVars, serverMaps);
            KeyMap<org.jline.reader.Binding> keymap = lineReader.getKeyMaps().get(LineReader.MAIN);
            keymap.bind(new Reference("beginning-of-line"), "\033[1~");
            keymap.bind(new Reference("end-of-line"), "\033[4~");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        WelcomeInfo.welcome();

        while (true) {
            try {

                String prompt = "[" + consoleInitializer.getCurrentServer().getKey() + "]> ";
                String request = lineReader.readLine(prompt);

                if (lineReader == null) {
                    System.out.println("Console can not read commands.");
                    break;
                }

                String[] params = null;
                params = ConsoleUtils.tokenizeCommand(request);
                if (params.length < 1) {
                    System.out.print("");
                    continue;
                }
                if ("".equals(params[0].trim())) {
                    System.out.print("");
                    continue;
                }
                if ("quit".equals(params[0]) || "q".equals(params[0])) {
                    if (HelpInfo.promptNoParams(params, "q")) {
                        continue;
                    } else if (params.length > 2) {
                        HelpInfo.promptHelp("q");
                        continue;
                    }
                    break;
                }
                switch (params[0]) {
                    case "h":
                    case "help":
                        {
                            WelcomeInfo.help(params);
                            break;
                        }
                    case "currentServer":
                        {
                            if (HelpInfo.promptNoParams(params, "currentServer")) {
                                continue;
                            } else if (params.length > 2) {
                                HelpInfo.promptHelp("currentServer");
                                continue;
                            }
                            System.out.println(consoleInitializer.getCurrentServer());
                            System.out.println();
                            break;
                        }
                    case "listServers":
                        {
                            if (HelpInfo.promptNoParams(params, "listServers")) {
                                continue;
                            }
                            System.out.println(consoleInitializer.getWeCrossServers().getServers());
                            System.out.println();
                            break;
                        }
                    case "switch":
                        {
                            if (rpcFace.switchServer(params)) {
                                lineReader =
                                        JlineUtils.getLineReader(
                                                rpcFace.getPaths(),
                                                resourceVars,
                                                pathVars,
                                                serverMaps);
                                KeyMap<org.jline.reader.Binding> keymap =
                                        lineReader.getKeyMaps().get(LineReader.MAIN);
                                keymap.bind(new Reference("beginning-of-line"), "\033[1~");
                                keymap.bind(new Reference("end-of-line"), "\033[4~");
                                consoleInitializer.init(params[1]);
                                mockWeCross = new MockWeCross(consoleInitializer.getWeCrossRPC());
                                groovyShell.setProperty("WeCross", mockWeCross);
                            }
                            break;
                        }
                    case "listLocalResources":
                        {
                            if (HelpInfo.promptNoParams(params, "listLocalResources")) {
                                continue;
                            }
                            String listParams[] = {"list", "1"};
                            rpcFace.listResources(listParams);
                            break;
                        }
                    case "listResources":
                        {
                            if (HelpInfo.promptNoParams(params, "listResources")) {
                                continue;
                            }
                            String listParams[] = {"list", "0"};
                            rpcFace.listResources(listParams);
                            break;
                        }
                    case "exists":
                        {
                            rpcFace.existsResource(params, pathMaps);
                            break;
                        }
                    case "call":
                        {
                            rpcFace.call(params, pathMaps);
                            break;
                        }
                    case "send":
                    case "sendTransaction":
                        {
                            rpcFace.sendTransaction(params, pathMaps);
                            break;
                        }
                    default:
                        {
                            try {
                                if (ConsoleUtils.parseVars(
                                        params, resourceVars, pathVars, pathMaps)) {
                                    lineReader =
                                            JlineUtils.getLineReader(
                                                    rpcFace.getPaths(),
                                                    resourceVars,
                                                    pathVars,
                                                    serverMaps);
                                    KeyMap<org.jline.reader.Binding> keymap =
                                            lineReader.getKeyMaps().get(LineReader.MAIN);
                                    keymap.bind(new Reference("beginning-of-line"), "\033[1~");
                                    keymap.bind(new Reference("end-of-line"), "\033[4~");
                                }

                                Object result =
                                        groovyShell.evaluate(ConsoleUtils.parseRequest(params));

                                // Object result = groovyShell.evaluate(request);
                                if (result != null) {
                                    System.out.println(
                                            "Result ==> " + mapper.writeValueAsString(result));
                                    System.out.println();
                                } else {
                                    System.out.println();
                                }
                            } catch (Exception e) {
                                System.out.println("Error: unsupported command.");
                                System.out.println();
                            }
                            break;
                        }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
    }
}
