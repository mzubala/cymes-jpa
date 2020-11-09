package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.Cinema;

import java.util.List;

public interface CinemaRepository {
    void save(Cinema cinema);

    List<Cinema> getAll();
}
