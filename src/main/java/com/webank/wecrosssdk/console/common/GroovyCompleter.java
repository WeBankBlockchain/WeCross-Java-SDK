package com.webank.wecrosssdk.console.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;

class GroovyCompleter implements Completer {

    protected final Collection<Candidate> candidates = new ArrayList<>();

    public GroovyCompleter() {}

    public GroovyCompleter(String... strings) {
        this(Arrays.asList(strings));
    }

    public GroovyCompleter(Iterable<String> strings) {
        assert strings != null;
        for (String string : strings) {
            candidates.add(
                    new Candidate(
                            AttributedString.stripAnsi(string),
                            string,
                            null,
                            null,
                            null,
                            null,
                            true));
        }
    }

    public void complete(
            LineReader reader, final ParsedLine commandLine, final List<Candidate> candidates) {
        assert commandLine != null;
        assert candidates != null;

        String word = commandLine.word();
        if (word.endsWith("=")) {
            candidates.addAll(this.candidates);
        }
    }
}
