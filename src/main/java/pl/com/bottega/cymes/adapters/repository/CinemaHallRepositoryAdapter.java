package pl.com.bottega.cymes.adapters.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import pl.com.bottega.cymes.domain.model.CinemaHall.Row;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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