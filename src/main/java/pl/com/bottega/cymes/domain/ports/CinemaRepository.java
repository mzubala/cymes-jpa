package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.Cinema;

import java.util.List;
import java.util.UUID;

public interface CinemaRepository {
    void save(Cinema cinema) throws CinemaExistsException;

    List<Cinema> getAll();

    Cinema get(UUID cinemaUUID) throws AggregateNotFoundException;

    class CinemaExistsException extends RuntimeException { }
}
