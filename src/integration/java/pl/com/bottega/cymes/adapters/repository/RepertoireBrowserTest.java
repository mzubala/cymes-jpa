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
import pl.com.bottega.cymes.domain.model.queries.ShowsQuery;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.RepertoireBrowser;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import static pl.com.bottega.cymes.domain.model.CinemaHall.Row;
import static pl.com.bottega.cymes.domain.ports.RepertoireBrowser.SearchedShow;

public class RepertoireBrowserTest extends SpringAdapterTest {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private RepertoireBrowser repertoireBrowser;

    private Cinema lublinPlaza = new Cinema(UUID.randomUUID(), "Lublin", "Plaza");
    private CinemaHall lbnHall1 = new CinemaHall(new CinemaHallId(lublinPlaza.getId(), "1"), List.of(new Row("X")));
    private CinemaHall lbnHall2 = new CinemaHall(new CinemaHallId(lublinPlaza.getId(), "2"), List.of(new Row("X")));
    private Cinema warszawaArkadia = new Cinema(UUID.randomUUID(), "Warszawa", "Arkadia");
    private CinemaHall arkadiaHall = new CinemaHall(new CinemaHallId(warszawaArkadia.getId(), "1"), List.of(new Row("X")));
    private Cinema warszawaZlote = new Cinema(UUID.randomUUID(), "Warszawa", "Złote Tarasy");
    private CinemaHall zloteHall = new CinemaHall(new CinemaHallId(warszawaZlote.getId(), "1"), List.of(new Row("X")));
    private Movie batman = MovieExample.builder().title("Batman").build().toDomain();
    private Movie pulpFiction = MovieExample.builder().title("Pulp Fiction").build().toDomain();
    private List<Show> shows;

    @BeforeEach
    public void setup() {
        super.setup();
        cinemaRepository.save(lublinPlaza);
        cinemaRepository.save(warszawaArkadia);
        cinemaRepository.save(warszawaZlote);
        movieRepository.save(batman);
        movieRepository.save(pulpFiction);
        cinemaHallRepository.save(lbnHall1);
        cinemaHallRepository.save(lbnHall2);
        cinemaHallRepository.save(zloteHall);
        cinemaHallRepository.save(arkadiaHall);
        setupShows();
    }

    private void setupShows() {
        shows = List.of(
                show(lbnHall1, batman, "2020-01-01T10:00:00"), //0
                show(lbnHall1, batman, "2020-01-01T12:00:00"), //1
                show(lbnHall1, batman, "2020-01-02T14:00:00"), //2
                show(lbnHall1, batman, "2020-01-02T16:00:00"), //3
                show(lbnHall2, batman, "2020-01-01T11:00:00"), //4
                show(lbnHall2, batman, "2020-01-01T13:00:00"), //5
                show(lbnHall2, pulpFiction, "2020-01-01T15:00:00"), //6
                show(lbnHall2, pulpFiction, "2020-01-02T15:00:00"), //7
                show(lbnHall2, pulpFiction, "2020-01-02T17:00:00"), //8
                show(arkadiaHall, batman, "2020-01-01T10:00:00"), //9
                show(arkadiaHall, batman, "2020-01-01T12:00:00"), //10
                show(arkadiaHall, batman, "2020-01-02T14:00:00"), //11
                show(arkadiaHall, batman, "2020-01-02T16:00:00"), //12
                show(arkadiaHall, pulpFiction, "2020-01-02T18:00:00"), //13
                show(arkadiaHall, pulpFiction, "2020-01-02T20:00:00"), //14
                show(zloteHall, batman, "2020-01-01T11:00:00"), //15
                show(zloteHall, batman, "2020-01-01T13:00:00"), //16
                show(zloteHall, pulpFiction, "2020-01-02T20:00:00") //17
        );
    }

    @Test
    void searchesShowsByDateCinemaAndMovie() {
        var result = repertoireBrowser.search(new ShowsQuery(LocalDate.of(2020, 1, 1), null, lublinPlaza.getId(), batman.getId()));

        assertThat(result).containsExactly(
                searchedShow(0, batman, lublinPlaza),
                searchedShow(4, batman, lublinPlaza),
                searchedShow(1, batman, lublinPlaza),
                searchedShow(5, batman, lublinPlaza)
        );
    }

    @Test
    void searchesShowsByDateAndCinema() {
        var result = repertoireBrowser.search(new ShowsQuery(LocalDate.of(2020, 1, 1), null, lublinPlaza.getId(), null));

        assertThat(result).containsExactly(
                searchedShow(0, batman, lublinPlaza),
                searchedShow(4, batman, lublinPlaza),
                searchedShow(1, batman, lublinPlaza),
                searchedShow(5, batman, lublinPlaza),
                searchedShow(6, pulpFiction, lublinPlaza)
        );
    }

    @Test
    void searchesShowsByDateAndCity() {
        var result = repertoireBrowser.search(new ShowsQuery(LocalDate.of(2020, 1, 1), "Warszawa", null, null));

        assertThat(result).containsExactly(
                searchedShow(9, batman, warszawaArkadia),
                searchedShow(15, batman, warszawaZlote),
                searchedShow(10, batman, warszawaArkadia),
                searchedShow(16, batman, warszawaZlote)
        );
    }

    @Test
    void searchesShowsByDateAndCityAndMovie() {
        var result = repertoireBrowser.search(new ShowsQuery(LocalDate.of(2020, 1, 2), "Warszawa", null, pulpFiction.getId()));

        assertThat(result).containsExactly(
                searchedShow(13, pulpFiction, warszawaArkadia),
                searchedShow(14, pulpFiction, warszawaArkadia),
                searchedShow(17, pulpFiction, warszawaZlote)
        );
    }

    private Show show(CinemaHall cinemaHall, Movie movie, String start) {
        Instant s = Instant.parse(start + "Z");
        Instant e = s.plus(2, ChronoUnit.HOURS);
        Show show = new Show(UUID.randomUUID(), cinemaHall.getId(), movie.getId(), s, e);
        showRepository.save(show);
        return show;
    }

    private SearchedShow searchedShow(int index, Movie movie, Cinema cinema) {
        return new SearchedShow(
                shows.get(index).getId(),
                movie.getTitle(),
                cinema.getName(),
                cinema.getCity(),
                cinema.getId(),
                shows.get(index).getStartAt()
        );
    }

}
