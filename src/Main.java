package cover;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {

        final List<Set> coveringSets = new ArrayList<>();
        // To make indexes start from 1
        coveringSets.add(new EmptySet());

        class SetHolder {
            Set set;

            SetHolder() {
                this.set = new EmptySet();
            }

            void add(Set setToAdd) {
                set = set.add(setToAdd);
            }

            void flush() {
                coveringSets.add(set);
                this.set = new EmptySet();
            }
        }

        final SetHolder currentlyBuilt = new SetHolder();

        Map<Pattern, Consumer<List<Integer>>> map = new HashMap<>();

        map.put(new Pattern(1),
                input -> currentlyBuilt.add(new Element(input.get(0))));
        map.put(new Pattern(1, -1),
                input -> currentlyBuilt.add(new RangeSet(input.get(0), -input.get(1))));
        map.put(new Pattern(1, -1, -1),
                input -> currentlyBuilt.add(new RangeSet(input.get(0), -input.get(1), -input.get(2))));
        map.put(new Pattern(0),
                input -> currentlyBuilt.flush());
        map.put(new Pattern(-1, 1),
                input -> {
                    Set toCover = new RangeSet(1, 1, -input.get(0));
                    int method = input.get(1);

                    Solver solver = null;
                    if (method == 1)
                        solver = new Solver1(coveringSets, toCover);
                    else if (method == 2)
                        solver = new Solver2(coveringSets, toCover);
                    else if (method == 3)
                        solver = new Solver3(coveringSets, toCover);
                    else
                        assert (false);

                    Solution solution = solver.solve();
                    System.out.println(solution);
                });

        PatternList patterns = new PatternList();
        patterns.addAll(map.keySet());

        Parser parser = new Parser(patterns);

        Optional<Match> next;

        do {
            next = parser.next();
            next.ifPresent(match -> map.get(match.pattern).accept(match.input));
        } while (next.isPresent());

    }
}
