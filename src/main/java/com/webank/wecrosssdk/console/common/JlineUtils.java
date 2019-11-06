package com.webank.wecrosssdk.console.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Attributes;
import org.jline.terminal.Attributes.ControlChar;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class JlineUtils {

    public static LineReader getLineReader(
            List<String> paths, Set<String> resourceVars, Set<String> pathVars) throws IOException {

        List<Completer> completers = new ArrayList<Completer>();

        // commands
        List<String> commands =
                Arrays.asList(
                        "help",
                        "quit",
                        "currentServer",
                        "listServers",
                        "switch",
                        "list",
                        "exists",
                        "call",
                        "sendTransaction");

        for (String command : commands) {
            completers.add(
                    new ArgumentCompleter(
                            new IgnoreCaseCompleter(command), new StringsCompleter()));
        }

        // resourceVars
        for (String var : resourceVars) {
            completers.add(new ArgumentCompleter(new StringsCompleter(var + ".exists()")));
            completers.add(
                    new ArgumentCompleter(
                            new StringsCompleter(var + ".call"), new StringsCompleter()));
            completers.add(
                    new ArgumentCompleter(
                            new StringsCompleter(var + ".sendTransaction"),
                            new StringsCompleter()));
        }

        // pathVars
        for (String var : pathVars) {
            completers.add(new ArgumentCompleter(new StringsCompleter(var)));
            completers.add(
                    new ArgumentCompleter(
                            new GroovyCompleter("WeCross.getResource"),
                            new StringsCompleter(var),
                            new StringsCompleter()));
        }

        for (String path : paths) {
            completers.add(
                    new ArgumentCompleter(new GroovyCompleter(path), new StringsCompleter()));
            completers.add(
                    new ArgumentCompleter(
                            new GroovyCompleter("WeCross.getResource"),
                            new StringsCompleter("\"" + path + "\""),
                            new StringsCompleter()));
        }

        commands = Arrays.asList("exists", "call", "sendTransaction");
        for (String command : commands) {
            for (String path : paths) {
                completers.add(
                        new ArgumentCompleter(
                                new StringsCompleter(command),
                                new StringsCompleter(path),
                                new StringsCompleter()));
            }
        }
        for (String command : commands) {
            for (String path : pathVars) {
                completers.add(
                        new ArgumentCompleter(
                                new StringsCompleter(command),
                                new StringsCompleter(path),
                                new StringsCompleter()));
            }
        }

        Terminal terminal =
                TerminalBuilder.builder()
                        .nativeSignals(true)
                        .signalHandler(Terminal.SignalHandler.SIG_IGN)
                        .build();
        Attributes termAttribs = terminal.getAttributes();
        // enable CTRL+D shortcut to exit
        // disable CTRL+C shortcut
        termAttribs.setControlChar(ControlChar.VEOF, 4);
        termAttribs.setControlChar(ControlChar.VINTR, -1);
        terminal.setAttributes(termAttribs);

        return LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new AggregateCompleter(completers))
                .build()
                .option(LineReader.Option.HISTORY_IGNORE_SPACE, false)
                .option(LineReader.Option.HISTORY_REDUCE_BLANKS, false);
    }
}
