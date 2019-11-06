package com.webank.wecrosssdk.console.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jline.reader.Buffer;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;

public class IgnoreCaseCompleter implements Completer {

    protected final Collection<Candidate> candidates = new ArrayList<>();

    public IgnoreCaseCompleter() {}

    public IgnoreCaseCompleter(String... strings) {
        this(Arrays.asList(strings));
    }

    public IgnoreCaseCompleter(Iterable<String> strings) {
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

    public IgnoreCaseCompleter(Candidate... candidates) {
        assert candidates != null;
        this.candidates.addAll(Arrays.asList(candidates));
    }

    public void complete(
            LineReader reader, final ParsedLine commandLine, final List<Candidate> candidates) {
        assert commandLine != null;
        assert candidates != null;

        Buffer buffer = reader.getBuffer();
        String start = (buffer == null) ? "" : buffer.toString();
        int index = start.lastIndexOf(" ");
        String tmp = start.substring(index + 1).toLowerCase();

        for (Iterator<Candidate> iter = this.candidates.iterator(); iter.hasNext(); ) {
            Candidate candidate = iter.next();
            String candidateStr = candidate.value().toLowerCase();
            if (candidateStr.startsWith(tmp)) {
                candidates.add(candidate);
            }
        }
    }
}
