package cover;

import java.util.Optional;

// a sorted SimpleIterator, which is an intersection of two sorted SimpleIterators
class IntersectIterator implements SimpleIterator<Integer> {

    private PlusIterator it;

    public IntersectIterator(SimpleIterator<Integer> it0, SimpleIterator<Integer> it1) {
        it = new PlusIterator(it0, it1);
    }

    public Optional<Integer> next() {
        // iterate through the sum of it0 and it1 and try to find a common element
        Optional<Integer> next;
        do {
            if (it.end(0) || it.end(1))
                // an optimization - stop if one of the iterators has already ended
                next = Optional.empty();
            else
                next = it.next();
            // repeat until [it] ends or there is an element from both it0 and it1
        } while (next.isPresent() && !(it.isFrom[0] && it.isFrom[1]));
        return next;
    }
}

public class Intersection extends Set {

    private Set a;
    private Set b;

    public Intersection(Set a, Set b) {
        this.a = a;
        this.b = b;
    }

    public SimpleIterator<Integer> iterator() {
        return new IntersectIterator(a.iterator(), b.iterator());
    }
}
