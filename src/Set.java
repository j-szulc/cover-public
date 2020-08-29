package cover;

import java.util.Optional;

public abstract class Set {

    public Set subtract(Set s) {
        return new Minus(this, s);
    }

    public Set add(Set s) {
        return new Plus(this, s);
    }

    public Set intersect(Set s) {
        return new Intersection(this, s);
    }

    public abstract SimpleIterator<Integer> iterator();

    public boolean isEmpty() {
        return iterator().next().isEmpty();
    }

    public boolean disjointWith(Set s) {
        return intersect(s).isEmpty();
    }

    public int size() {
        int i = 0;
        SimpleIterator<Integer> it = iterator();
        while (it.next().isPresent())
            i++;
        return i;
    }

    protected static final int MAX_STRING_DEPTH = 100;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        SimpleIterator<Integer> it = iterator();
        Optional<Integer> next = it.next();

        for (int i = 0; i < MAX_STRING_DEPTH && next.isPresent(); i++) {
            builder.append(next.get()).append(", ");
            next = it.next();
        }

        if (next.isPresent())
            builder.append("...");

        return builder.toString();
    }
}

class EmptySet extends Set {
    @Override
    public SimpleIterator<Integer> iterator() {
        return new SimpleIterator<Integer>() {
            @Override
            public Optional<Integer> next() {
                return Optional.empty();
            }
        };
    }

    @Override
    public Set intersect(Set s) {
        return new EmptySet();
    }

    public Set add(Set s) {
        return s;
    }

    public Set subtract(Set s) {
        return new EmptySet();
    }
}