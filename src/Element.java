package cover;

import java.util.Optional;

// a set containing only one element (singleton)
public class Element extends Set {

    private Integer element;

    public Element(Integer element) {
        this.element = element;
    }

    public SimpleIterator<Integer> iterator() {
        return new SimpleIterator<Integer>() {
            boolean end;

            public Optional<Integer> next() {
                if (!end) {
                    end = true;
                    return Optional.of(element);
                } else
                    return Optional.empty();
            }
        };
    }

}
