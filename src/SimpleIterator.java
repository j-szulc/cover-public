package cover;

import java.util.Optional;

public interface SimpleIterator<T> {
    // Returns either the next element or an empty option
    Optional<T> next();
}
