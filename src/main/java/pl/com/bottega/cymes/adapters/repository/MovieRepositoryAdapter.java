package pl.com.bottega.cymes.adapters.repository;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.PaginatedSearchResults;
import pl.com.bottega.cymes.domain.ports.MovieRepository;

import java.util.UUID;

@Component
public class MovieRepositoryAdapter implements MovieRepository {

    @Override
    public void save(Movie movie) {

    }

    @Override
    public Movie get(UUID id) {
        return null;
    }

    @Override
    public PaginatedSearchResults<BasicMovieInformation> search(BasicMovieQuery query) {
        return null;
    }
}