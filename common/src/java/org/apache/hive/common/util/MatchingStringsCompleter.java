package org.apache.hive.common.util;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A matching string Completer based on JLine2's StringCompleter
 */
public class MatchingStringsCompleter implements Completer {
  protected SortedSet<String> candidateStrings = new TreeSet<>();

  public MatchingStringsCompleter() {
    // empty
  }

  public MatchingStringsCompleter(String... strings) {
    this(Arrays.asList(strings));
  }

  public MatchingStringsCompleter(Iterable<String> strings) {
    strings.forEach(candidateStrings::add);
  }

  public MatchingStringsCompleter(Candidate... candidates) {
    Arrays.stream(candidates).map(Candidate::value).forEach(candidateStrings::add);
  }

  public Collection<String> getStrings() {
    return candidateStrings;
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    assert candidates != null;

    if (line == null) {
      candidateStrings.stream().map(Candidate::new).forEach(candidates::add);
    } else {
      for (String match : this.candidateStrings.tailSet(line.word())) {
        if (!match.startsWith(line.word())) {
          break;
        }
        candidates.add(new Candidate(match));
      }
    }
  }
}
