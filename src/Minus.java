package cover;

import java.util.Optional;

// a sorted SimpleIterator which is a difference of two sorted SimpleIterators
class MinusIterator implements SimpleIterator<Integer> {

    private PlusIterator it;

    public MinusIterator(SimpleIterator<Integer> it0, SimpleIterator<Integer> it1) {
        it = new PlusIterator(it0, it1);
    }

    public Optional<Integer> next() {
        // iterate through the sum of it0 and it1 and try to find an element
        // which is in it0, but isn't in it1
        Optional<Integer> next;
        do {
            if (it.end(0))
                // an optimization - stop if it0 has ended
                next = Optional.empty();
            else
                next = it.next();
        } while (next.isPresent() && !(it.isFrom[0] && !it.isFrom[1]));
        return next;
    }
}

public class Minus extends Set {

    private Set a;
    private Set b;

    public Minus(Set a, Set b) {
        this.a = a;
        this.b = b;
    }

    public SimpleIterator<Integer> iterator() {
        return new MinusIterator(a.iterator(), b.iterator());
    }
}
