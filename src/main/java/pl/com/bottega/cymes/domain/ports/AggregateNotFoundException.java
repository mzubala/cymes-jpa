package pl.com.bottega.cymes.domain.ports;

import static java.lang.String.format;

public class AggregateNotFoundException extends RuntimeException {
    public AggregateNotFoundException(Class aggregateClass, Object id) {
        super(format("Cannot find aggregate %s with id %s", aggregateClass.getSimpleName(), id));
    }
}
