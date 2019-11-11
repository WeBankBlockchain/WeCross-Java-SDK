package com.webank.wecrosssdk.console.common;

import com.webank.wecrosssdk.exception.ConsoleException;
import com.webank.wecrosssdk.exception.Status;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class ConsoleUtils {

    public static boolean isValidPathVar(String path, Map<String, String> pathMaps) {
        if (pathMaps.containsKey(path)) {
            return true;
        }
        return false;
    }

    // parse variables and save path variables
    public static Boolean parseVars(
            String params[],
            Set<String> resourceVars,
            Set<String> pathVars,
            Map<String, String> pathMaps) {
        Integer length = params.length;
        if (length < 3 || params[0].contains("\"") || params[0].contains("\'")) {
            return false;
        }

        if (params[1].equals("=")) {
            if (params[2].equals("WeCross.getResource")) {
                if (length != 4) {
                    if (length > 5 || !params[4].equals(" ")) {
                        return false;
                    }
                }
                if (pathVars.contains(params[3])) {
                    resourceVars.add(params[0]);
                    return true;
                }

                String out = parseString(params[3]);
                if (RPCUtils.isValidPath(out)) {
                    resourceVars.add(params[0]);
                    return true;
                }
            } else {
                if (length != 3) {
                    if (length > 4 || !params[3].equals(" ")) {
                        return false;
                    }
                }
                String out = parseString(params[2]);
                if (RPCUtils.isValidPath(out)) {
                    pathVars.add(params[0]);
                    pathMaps.put(params[0], out);
                    return true;
                }
            }
        }
        return false;
    }

    // remove "" or '' of string
    public static String parseString(String input) {
        Integer len = input.length();
        if (len < 2) {
            return input;
        }
        if (input.charAt(0) == '\"' && input.charAt(len - 1) == '\"'
                || input.charAt(0) == '\'' && input.charAt(len - 1) == '\'') {
            return input.substring(1, len - 1);
        }
        return input;
    }

    // parse args as string or int
    public static Object[] parseArgs(String params[], int start) throws ConsoleException {
        Integer length = params.length;
        Object ret[] = new Object[length - start];
        Integer i = 0, j = start;
        for (; j < length; ++j) {
            Integer len = params[j].length();
            if (params[j].charAt(0) == '\"' && params[j].charAt(len - 1) == '\"'
                    || params[j].charAt(0) == '\'' && params[j].charAt(len - 1) == '\'') {
                // as string
                ret[i++] = params[j].substring(1, len - 1);
            } else {
                // as int
                try {
                    ret[i++] = Integer.parseInt(params[j]);
                } catch (Exception e) {
                    String errorMessage = "Cannot convert " + params[j] + " to int";
                    throw new ConsoleException(Status.INTERNAL_ERROR, errorMessage);
                }
            }
        }
        return ret;
    }

    public static void printJson(String jsonStr) {
        System.out.println(formatJson(jsonStr));
    }

    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        jsonStr = jsonStr.replace("\\n", "");
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                case ' ':
                    if (',' != jsonStr.charAt(i - 1)) {
                        sb.append(current);
                    }
                    break;
                case '\\':
                    break;
                default:
                    if (!(current == " ".charAt(0))) sb.append(current);
            }
        }

        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("    ");
        }
    }

    private static class CommandTokenizer extends StreamTokenizer {
        public CommandTokenizer(Reader r) {
            super(r);
            resetSyntax();
            // Invisible ASCII characters.
            whitespaceChars(0x00, 0x20);
            // All visible ASCII characters.
            wordChars(0x21, 0x7E);
            // Other UTF8 characters.
            wordChars(0xA0, 0xFF);
            // Uncomment this to allow comments in the command.
            // commentChar('/');
            // Allow both types of quoted strings, e.g. 'abc' and "abc".
            quoteChar('\'');
            quoteChar('"');
        }

        public void parseNumbers() {}
    }

    public static String[] tokenizeCommand(String command) throws Exception {
        // example: callByCNS HelloWorld.sol set"Hello" parse [callByCNS, HelloWorld.sol,
        // set"Hello"]
        List<String> tokens1 = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(command, " ");
        while (stringTokenizer.hasMoreTokens()) {
            tokens1.add(stringTokenizer.nextToken());
        }
        // example: callByCNS HelloWorld.sol set"Hello" parse [callByCNS, HelloWorld.sol, set,
        // "Hello"]
        List<String> tokens2 = new ArrayList<>();
        StreamTokenizer tokenizer = new CommandTokenizer(new StringReader(command));
        int token = tokenizer.nextToken();
        while (token != StreamTokenizer.TT_EOF) {
            switch (token) {
                case StreamTokenizer.TT_EOL:
                    // Ignore \n character.
                    break;
                case StreamTokenizer.TT_WORD:
                    tokens2.add(tokenizer.sval);
                    break;
                case '\'':
                    // If the tailing ' is missing, it will add a tailing ' to it.
                    // E.g. 'abc -> 'abc'
                    tokens2.add(String.format("'%s'", tokenizer.sval));
                    break;
                case '"':
                    // If the tailing " is missing, it will add a tailing ' to it.
                    // E.g. "abc -> "abc"
                    tokens2.add(String.format("\"%s\"", tokenizer.sval));
                    break;
                default:
                    // Ignore all other unknown characters.
                    throw new RuntimeException("unexpected input tokens " + token);
            }
            token = tokenizer.nextToken();
        }
        return tokens1.size() <= tokens2.size()
                ? tokens1.toArray(new String[tokens1.size()])
                : tokens2.toArray(new String[tokens2.size()]);
    }

    public static String parseRequest(String[] params) {
        String result = "";
        for (String param : params) {
            String temp = parseString(param);
            if (RPCUtils.isValidPath(temp)) {
                result += ("\"" + temp + "\"" + " ");
            } else {
                result += (param + " ");
            }
        }
        return result.substring(0, result.length() - 1);
    }

    public static void singleLine() {
        System.out.println(
                "---------------------------------------------------------------------------------------------");
    }

    public static void doubleLine() {
        System.out.println(
                "=============================================================================================");
    }
}
