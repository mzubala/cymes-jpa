package pl.com.bottega.cymes.adapters.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.adapters.SpringAdapterTest;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.examples.MovieExample;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.adapters.rest.ShowsResource.*;

public class ShowsResourceTest extends SpringAdapterTest {

    private Cinema lublinPlaza = new Cinema(UUID.randomUUID(), "Lublin", "Plaza");
    private CinemaHall lbnHall1 = new CinemaHall(new CinemaHall.CinemaHallId(lublinPlaza.getId(), "1"), List.of(new CinemaHall.Row("X")));
    private Movie batman = MovieExample.builder().title("Batman").build().toDomain();

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private int usersCount = 10;

    private ExecutorService executorService = Executors.newFixedThreadPool(usersCount);

    @BeforeEach
    public void setup() {
        super.setup();
        movieRepository.save(batman);
        cinemaRepository.save(lublinPlaza);
        cinemaHallRepository.save(lbnHall1);
    }

    @Test
    public void doesNotIntroduceConflictingShows() {
        for (int i = 0; i < usersCount; i++) {
            var resp = cymesClient.trySchedulingShow(new ScheduleShowRequest(
                    lublinPlaza.getId().toString(), lbnHall1.getId().getNumber(), batman.getId().toString(), Instant.now().plus(1, DAYS)
            ));
        }

        assertThat(entityManager.createQuery("SELECT count(show) FROM Show show").getSingleResult()).isEqualTo(1L);
    }

}
