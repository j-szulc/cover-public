package cover;

import java.util.Optional;

// a sorted SimpleIterator which is a sum of two sorted SimpleIterators
class PlusIterator implements SimpleIterator<Integer> {

    // the two iterators to be summed
    private SimpleIterator<Integer>[] it;

    // an array of length 2, cached next elements from it[0] and it[1] respectively
    private Optional<Integer>[] next;

    // an array of length 2, says whether the previously returned value was from it0 or it1 or both
    public boolean[] isFrom;

    @SuppressWarnings("unchecked")
    public PlusIterator(SimpleIterator<Integer> it0, SimpleIterator<Integer> it1) {
        this.it = new SimpleIterator[]{it0, it1};
        next = new Optional[]{it0.next(), it1.next()};
        isFrom = new boolean[]{false, false};
    }

    // [next] function decides from which [it] to get the next element
    // and after deciding invokes this method
    private Optional<Integer> getNext(int i) {
        isFrom[i] = true;
        Optional<Integer> result = next[i];
        next[i] = it[i].next();
        return result;
    }

    // says if it[i] has ended
    public boolean end(int i) {
        return next[i].isEmpty();
    }

    public Optional<Integer> next() {
        isFrom = new boolean[]{false, false};
        if (next[0].isEmpty())
            return getNext(1);
        else if (next[1].isEmpty())
            return getNext(0);
        else {
            if (next[0].get() < next[1].get())
                return getNext(0);
            else if (next[0].get() > next[1].get())
                return getNext(1);
            else {
                getNext(0);
                return getNext(1);
            }
        }
    }
}

public class Plus extends Set {

    private Set a;
    private Set b;

    public Plus(Set a, Set b) {
        this.a = a;
        this.b = b;
    }

    public SimpleIterator<Integer> iterator() {
        return new PlusIterator(a.iterator(), b.iterator());
    }
}
