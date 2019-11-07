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

public class GroovyCompleter implements Completer {

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

    public GroovyCompleter(Candidate... candidates) {
        assert candidates != null;
        this.candidates.addAll(Arrays.asList(candidates));
    }

    public void complete(
            LineReader reader, final ParsedLine commandLine, final List<Candidate> candidates) {
        assert commandLine != null;
        assert candidates != null;

        String buffer = reader.getBuffer().toString();
        Candidate candidate = new Candidate(buffer);
        if (!buffer.contains(" ")) {
            candidates.add(candidate);
        }

        //        int index = buffer.lastIndexOf(" ");
        //        String tmp = buffer.substring(index + 1);
        //
        //        for (Iterator<Candidate> iter = this.candidates.iterator(); iter.hasNext(); ) {
        //            Candidate candidate = iter.next();
        //            String candidateStr = candidate.value();
        //            if (buffer.contains("= ") && candidateStr.startsWith(tmp)) {
        //                candidates.add(candidate);
        //            }
        //        }
    }
}
