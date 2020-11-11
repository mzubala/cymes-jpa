package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.adapters.rest.MoviesResource;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.PaginatedSearchResults;

import java.util.List;
import java.util.UUID;

public interface MovieRepository {
    void save(Movie movie);
    Movie get(UUID id);
    PaginatedSearchResults<BasicMovieInformation> search(BasicMovieQuery query);
}
