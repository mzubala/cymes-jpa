package pl.com.bottega.cymes.adapters.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.adapters.SpringAdapterTest;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.Show;
import pl.com.bottega.cymes.domain.model.examples.MovieExample;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import static pl.com.bottega.cymes.domain.model.CinemaHall.Row;

public class ShowRepositoryAdapterTest extends SpringAdapterTest {

    private Cinema lublinPlaza = new Cinema(UUID.randomUUID(), "Lublin", "Plaza");
    private CinemaHall occupiedHall = new CinemaHall(new CinemaHallId(lublinPlaza.getId(), "1"), List.of(new Row("X")));
    private CinemaHall otherHall = new CinemaHall(new CinemaHallId(lublinPlaza.getId(), "2"), List.of(new Row("X")));
    private Movie batman = MovieExample.builder().title("Batman").build().toDomain();
    private Instant showStart = Instant.now();
    private Instant showEnd = Instant.now().plus(2, HOURS);
    private Show show = new Show(UUID.randomUUID(), occupiedHall.getId(), batman.getId(), Instant.now(), showEnd);

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private ShowRepository showRepository;

    @BeforeEach
    public void setup() {
        super.setup();
        movieRepository.save(batman);
        cinemaRepository.save(lublinPlaza);
        cinemaHallRepository.save(occupiedHall);
        cinemaHallRepository.save(otherHall);
        showRepository.save(show);
    }

    @Test
    public void checksShowsCollisions() {
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart, showEnd))).isTrue();
        assertThat(showRepository.containsShowsCollidingWith(show(otherHall, showStart, showEnd))).isFalse();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showEnd, showEnd.plus(2, HOURS)))).isFalse();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart.minus(2, HOURS), showStart))).isFalse();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart.minus(2, HOURS), showStart.plus(1, SECONDS)))).isTrue();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart.plus(1, SECONDS), showEnd.minus(1, SECONDS)))).isTrue();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart.plus(1, SECONDS), showEnd))).isTrue();
        assertThat(showRepository.containsShowsCollidingWith(show(occupiedHall, showStart.plus(1, SECONDS), showEnd))).isTrue();
    }

    private Show show(CinemaHall cinemaHall, Instant showStart, Instant showEnd) {
        return new Show(UUID.randomUUID(), cinemaHall.getId(), batman.getId(), showStart, showEnd);
    }
}
