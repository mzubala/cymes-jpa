package pl.com.bottega.cymes.adapters.repository;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;

import java.util.List;
import java.util.UUID;

@Component
public class CinemaHallRepositoryAdapter implements CinemaHallRepository {

    @Override
    public void save(CinemaHall cinemaHall) {

    }

    @Override
    public List<String> getCinemaHallNumbers(UUID fromString) {
        return null;
    }

    @Override
    public CinemaHall get(CinemaHallId id) {
        return null;
    }
}