package cover;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

// class representing a pattern of integer signs
class Pattern {

    // for example signs = {-1, 1, -1, -1}
    public int[] signs;

    public Pattern(int... signs) {
        this.signs = signs;
    }

    // checks if toCheck matches this pattern
    // for example {2,-3} matches: {}, {1}, {1,-1}, {1,-1,1}, {1,-1,-1}, ...
    public boolean match(List<Integer> toCheck) {
        for (int i = 0; i < Math.min(signs.length, toCheck.size()); i++) {
            if (Integer.signum(toCheck.get(i)) != signs[i])
                return false;
        }
        return true;
    }

    public boolean fullMatch(List<Integer> toCheck) {
        return toCheck.size() >= signs.length && match(toCheck);
    }
}

class PatternList extends ArrayList<Pattern> {

    // returns the longest pattern satisfying given predicate
    // or empty option if it doesn't exist
    public Optional<Pattern> longest(Predicate<Pattern> predicate) {
        Comparator<Pattern> cmp = Comparator.comparing(p -> p.signs.length);
        return this.stream().filter(predicate).max(cmp);
    }

    // returns the longest pattern that matches input
    // or empty option if it doesn't exist
    public Optional<Pattern> longestMatching(List<Integer> input) {
        return longest(pattern -> pattern.match(input));
    }
}

// class representing a match
// i.e. a list of integers and the pattern that this list matched
class Match {
    Pattern pattern;
    List<Integer> input;

    public Match(Pattern pattern, List<Integer> input) {
        this.pattern = pattern;
        this.input = input;
    }
}

public class Parser {

    // patterns to look for
    private PatternList patterns;

    // currently being built
    private List<Integer> buffer;

    private Scanner scanner;

    public Parser(PatternList patterns, Scanner scanner) {
        this.patterns = patterns;
        this.scanner = scanner;
        buffer = new ArrayList<>();
    }

    public Parser(PatternList patterns) {
        this(patterns, new Scanner(System.in));
    }

    public Optional<Match> next() {

        // The longest pattern that the user could potentially mean
        Optional<Pattern> longestMatching = patterns.longestMatching(buffer);

        // If it's false, then we don't know what the user means
        assert (longestMatching.isPresent());

        // Add ints to the buffer until the longest pattern that the user could mean gets fully matched
        while (!longestMatching.get().fullMatch(buffer) && scanner.hasNext()) {
            buffer.add(scanner.nextInt());
            longestMatching = patterns.longestMatching(buffer);

            // If it's false, then we don't know what the user means
            assert (longestMatching.isPresent());
        }

        int matchedLength = longestMatching.get().signs.length;

        if (buffer.size() >= matchedLength) {
            List<Integer> matchedInput = new ArrayList<>(buffer.subList(0, matchedLength));

            if (matchedLength > 0) {
                buffer.subList(0, matchedLength).clear();
            }

            return Optional.of(new Match(longestMatching.get(), matchedInput));
        } else {
            // longestMatching wasn't completed - it's possible if the second condition in while was false
            // in this case -> scanner.hasNext()
            return Optional.empty();
        }

    }
}