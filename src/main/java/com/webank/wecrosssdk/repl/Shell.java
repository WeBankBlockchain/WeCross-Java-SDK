package com.webank.wecrosssdk.repl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.io.IOException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Attributes;
import org.jline.terminal.Attributes.ControlChar;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Shell {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            // SerializationFeature.FAIL_ON_EMPTY_BEANS

            Binding binding = new Binding();
            GroovyShell groovyShell = new GroovyShell(binding);
            groovyShell.setProperty("WeCross", new MockWeCross());

            Terminal terminal;

            terminal =
                    TerminalBuilder.builder()
                            .nativeSignals(true)
                            .signalHandler(Terminal.SignalHandler.SIG_IGN)
                            .build();

            Attributes termAttribs = terminal.getAttributes();
            termAttribs.setControlChar(ControlChar.VEOF, 4);
            termAttribs.setControlChar(ControlChar.VINTR, -1);
            terminal.setAttributes(termAttribs);
            LineReader lineReader =
                    LineReaderBuilder.builder()
                            .terminal(terminal)
                            .build()
                            .option(LineReader.Option.HISTORY_IGNORE_SPACE, false)
                            .option(LineReader.Option.HISTORY_REDUCE_BLANKS, false);

            while (true) {
                String request = lineReader.readLine("[WeCross: ]> ");
                try {
                    Object result = groovyShell.evaluate(request);
                    System.out.println("Result ==> " + mapper.writeValueAsString(result));
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }

    public static String test(String arg) {
        return "Hello world: " + arg;
    }
}
