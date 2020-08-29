package cover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class Solver {

    List<Set> coveringSets;
    Set toCover;

    public Solver(List<Set> coveringSets, Set toCover) {
        this.coveringSets = coveringSets;
        this.toCover = toCover;
    }

    public abstract Solution solve();
}

class Solution {

    private Optional<List<Integer>> list;

    public Solution(Optional<List<Integer>> list) {
        this.list = list;
    }

    public Solution(List<Integer> list) {
        this(Optional.of(list));
    }

    public Solution() {
        this(Optional.empty());
    }

    public String toString() {
        if (list.isEmpty())
            return "0";
        else {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<Integer> it = list.get().iterator();
            while (it.hasNext()) {
                stringBuilder.append(it.next());
                if (it.hasNext())
                    stringBuilder.append(" ");
            }
            return stringBuilder.toString();
        }
    }

    // Unfortunately it's not possible to extend Optional<List<Integer>>
    // because Optional is final
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean isPresent() {
        return list.isPresent();
    }

    public int size() {
        return list.map(List::size).orElse(-1);
    }
}

class Solver1 extends Solver {

    public Solver1(List<Set> coveringSets, Set toCover) {
        super(coveringSets, toCover);
    }

    // solve only using sets from coveringSets.sublist(startingIndex, coveringSets.size())
    // acc -> accumulator - the solution to this point
    private Solution solve(int startingIndex, List<Integer> acc) {
        if (toCover.isEmpty())
            return new Solution(acc);
        else if (startingIndex >= coveringSets.size())
            return new Solution();
        else {

            Set current = coveringSets.get(startingIndex);

            Solution resultWithout = solve(startingIndex + 1, acc);
            if (current.disjointWith(toCover))
                return resultWithout;

            List<Integer> accWith = new ArrayList<>(acc);
            accWith.add(startingIndex);
            Solution resultWith = new Solver1(coveringSets, toCover.subtract(current)).solve(startingIndex + 1, accWith);

            if (resultWithout.isEmpty())
                return resultWith;
            else {
                // if it's possible to solve without using current
                // then it should also be possible to solve using current
                assert (resultWith.isPresent());

                if (resultWithout.size() < resultWith.size())
                    // resultWithout is the only optimal solution
                    return resultWithout;
                else
                    // resultWithout and resultWith are both optimal
                    // but resultWith is first in lexicographical order
                    return resultWith;
            }
        }
    }

    @Override
    public Solution solve() {
        return solve(0, new ArrayList<>());
    }

}


class Solver2 extends Solver {

    public Solver2(List<Set> coveringSets, Set toCover) {
        super(coveringSets, toCover);
    }

    // return the index of the maximal element in [list] according to [cmp]
    private <T> Optional<Integer> maxIndex(List<T> list, Comparator<T> cmp) {
        if (list.isEmpty())
            return Optional.empty();
        else {
            int bestIndex = 0;

            for (int i = 1; i < list.size(); i++) {
                if (cmp.compare(list.get(i), list.get(bestIndex)) > 0)
                    bestIndex = i;
            }
            return Optional.of(bestIndex);
        }
    }

    @Override
    public Solution solve() {

        // we'll be removing sets that have already been used
        // to optimize, so a copy of the coveringSets list is needed
        coveringSets = new ArrayList<>(coveringSets);

        List<Integer> solution = new ArrayList<>();

        // best -> one of the coveringSets with the largest intersection with toCover
        Optional<Integer> bestIndex;
        int bestIntersectSize;

        do {

            final Set finalToCover = toCover;
            Function<Set, Integer> intersectSize = set -> set.intersect(finalToCover).size();
            Comparator<Set> intersectCmp = Comparator.comparing(intersectSize);

            bestIndex = maxIndex(coveringSets, intersectCmp);
            bestIntersectSize = 0;

            if (bestIndex.isPresent()) {
                Set best = coveringSets.get(bestIndex.get());
                bestIntersectSize = intersectSize.apply(best);

                solution.add(bestIndex.get());
                toCover = toCover.subtract(best);

                // optimization - make the already used set empty
                coveringSets.set(bestIndex.get(), new EmptySet());
            }

        } while (bestIntersectSize > 0 && !toCover.isEmpty());

        if (toCover.isEmpty()) {
            Collections.sort(solution);
            return new Solution(solution);
        } else
            return new Solution();
    }
}

class Solver3 extends Solver {

    public Solver3(List<Set> coveringSets, Set toCover) {
        super(coveringSets, toCover);
    }

    @Override
    public Solution solve() {
        List<Integer> solution = new ArrayList<>();
        Iterator<Set> it = coveringSets.iterator();

        for (int i = 0; it.hasNext() && !toCover.isEmpty(); i++) {
            Set s = it.next();
            if (!s.disjointWith(toCover)) {
                solution.add(i);
                toCover = toCover.subtract(s);
            }
        }

        if (toCover.isEmpty())
            return new Solution(solution);
        else
            return new Solution();
    }

}


