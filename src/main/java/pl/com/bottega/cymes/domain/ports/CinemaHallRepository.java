package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;

import java.util.List;
import java.util.UUID;

public interface CinemaHallRepository {
    void save(CinemaHall cinemaHall);

    List<String> getCinemaHallNumbers(UUID fromString);

    CinemaHall get(CinemaHallId id);
}
