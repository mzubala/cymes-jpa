package pl.com.bottega.cymes.domain.ports.impl;

import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.Show;
import pl.com.bottega.cymes.domain.model.commands.ScheduleShowCommand;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.ShowRepository;
import pl.com.bottega.cymes.domain.ports.ShowScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

public class DefaultShowScheduler implements ShowScheduler {
    public static final Duration COMMERCIAL_BUFFER = Duration.of(45, MINUTES);

    private final CinemaHallRepository cinemaHallRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public DefaultShowScheduler(CinemaHallRepository cinemaHallRepository, MovieRepository movieRepository, ShowRepository showRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void schedule(ScheduleShowCommand command) {
        var cinemaHall = cinemaHallRepository.get(command.getCinemaHallId());
        var movie = movieRepository.get(command.getMovieId());
        var show = new Show(UUID.randomUUID(), cinemaHall.getId(), movie.getId(), command.getStartTime(), endTime(command, movie));
        if(showRepository.containsShowsCollidingWith(show)) {
            throw new CinemaHallOccupiedException();
        }
        showRepository.save(show);
    }

    private Instant endTime(ScheduleShowCommand command, Movie movie) {
        return command.getStartTime().plus(movie.getDuration()).plus(COMMERCIAL_BUFFER);
    }
}
