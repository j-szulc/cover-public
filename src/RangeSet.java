package cover;

import java.util.Optional;

// a set representing an arithmetic sequence
// start, start+step, ..., start+k*step
// where k is such that start+k*step <= stop.get()
// or an infinite arithmetic sequence if stop is Empty
public class RangeSet extends Set {

    private Integer start;
    private Integer step;

    private Optional<Integer> stop;

    private RangeSet(Integer start, Integer step, Optional<Integer> stop) {
        this.start = start;
        this.step = step;
        this.stop = stop;
    }

    public RangeSet(Integer start, Integer step, Integer stop) {
        this(start, step, Optional.of(stop));
    }

    public RangeSet(Integer start, Integer step) {
        this(start, step, Optional.empty());
    }

    public SimpleIterator<Integer> iterator() {
        return new SimpleIterator<Integer>() {
            Integer next = start;

            public Optional<Integer> next() {
                if (stop.isEmpty() || next <= stop.get()) {
                    Integer result = next;
                    next += step;
                    return Optional.of(result);
                } else
                    return Optional.empty();
            }
        };
    }

    @Override
    public String toString() {
        if (stop.isEmpty())
            return start + ", " + (start + step) + ", ...";
        else
            return super.toString();
    }
}
