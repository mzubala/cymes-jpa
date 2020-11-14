package pl.com.bottega.cymes.adapters.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(AggregateNotFoundException.class)
    ResponseEntity<Errors> handle(AggregateNotFoundException aggregateNotFoundException) {
        return new ResponseEntity<>(Errors.of(aggregateNotFoundException), HttpStatus.NOT_FOUND);
    }
}
