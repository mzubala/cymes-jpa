package pl.com.bottega.cymes.domain.ports;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.commands.CreateCinemaCommand;
import pl.com.bottega.cymes.domain.model.commands.CreateMovieCommand;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.PaginatedSearchResults;

import java.util.List;

import static pl.com.bottega.cymes.domain.ports.CinemaRepository.CinemaExistsException;

@AllArgsConstructor
public class AdminService {

    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;

    public void createCinema(CreateCinemaCommand command) throws CinemaExistsException {
        var cinema = new Cinema(command.getId(), withoutSpaces(command.getCity()), withoutSpaces(command.getName()));
        cinemaRepository.save(cinema);
    }

    public List<Cinema> getCinemas() {
        return cinemaRepository.getAll();
    }

    public void createMovie(CreateMovieCommand command) {
        var movie = new Movie(command);
        movieRepository.save(movie);
    }

    private String withoutSpaces(String input) {
        return StringUtils.join(input.trim().split("\\s+"), " ");
    }

    public PaginatedSearchResults<BasicMovieInformation> search(BasicMovieQuery query) {
        return movieRepository.search(query);
    }
}
