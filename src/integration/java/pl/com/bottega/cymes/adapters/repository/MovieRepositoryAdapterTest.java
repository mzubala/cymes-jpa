package pl.com.bottega.cymes.adapters.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.adapters.SpringAdapterTest;
import pl.com.bottega.cymes.domain.model.examples.MovieExample;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.PaginatedSearchResults;
import pl.com.bottega.cymes.domain.model.queries.Pagination;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MovieRepositoryAdapterTest extends SpringAdapterTest {

    @Autowired
    private MovieRepositoryAdapter movieRepositoryAdapter;

    @Test
    public void createsAndReturnsAMovie() {
        // given
        var movie = MovieExample.builder().build().toDomain();

        // when
        movieRepositoryAdapter.save(movie);
        var fetchedMovie = movieRepositoryAdapter.get(movie.getId());

        // then
        assertThat(fetchedMovie).isEqualTo(movie);
    }

    @Test
    public void throwsExceptionWhenMovieDoesNotExist() {
        assertThatThrownBy(() -> movieRepositoryAdapter.get(UUID.randomUUID())).isInstanceOf(AggregateNotFoundException.class);
    }

    @Test
    public void returnsAllMoviesSortedByTitle() {
        // given
        var m1 = MovieExample.builder()
                .title("m2")
                .build().toDomain();
        var m2 = MovieExample.builder()
                .title("m1")
                .build().toDomain();
        movieRepositoryAdapter.save(m1);
        movieRepositoryAdapter.save(m2);

        // when
        var searchResults = movieRepositoryAdapter.search(new BasicMovieQuery(null, new Pagination(10, 1)));

        // then
        assertThat(searchResults).isEqualTo(new PaginatedSearchResults<BasicMovieInformation>(
                List.of(new BasicMovieInformation(m2.getId(), m2.getTitle()), new BasicMovieInformation(m1.getId(), m1.getTitle())),
                new Pagination(10, 1), 2L, 1L
        ));
    }

    @Test
    public void searchesMoviesByPhrase() {
        // given
        var m1 = MovieExample.builder()
                .title("Batman")
                .build().toDomain();
        var m2 = MovieExample.builder()
                .title("Pulp fiction")
                .build().toDomain();
        var m3 = MovieExample.builder()
                .title("Batman Forever")
                .build().toDomain();
        movieRepositoryAdapter.save(m1);
        movieRepositoryAdapter.save(m2);
        movieRepositoryAdapter.save(m3);

        // when
        var searchResults = movieRepositoryAdapter.search(new BasicMovieQuery("MAN", new Pagination(10, 1)));

        // then
        assertThat(searchResults).isEqualTo(new PaginatedSearchResults<BasicMovieInformation>(
                List.of(new BasicMovieInformation(m1.getId(), m1.getTitle()), new BasicMovieInformation(m3.getId(), m3.getTitle())),
                new Pagination(10, 1), 2L, 1L
        ));
    }

    @Test
    public void paginatesSearchResults() {
        // given
        int n = 50;
        List<BasicMovieInformation> movies = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            var movie = MovieExample.builder().title("m" + i).build().toDomain();
            movieRepositoryAdapter.save(movie);
            movies.add(new BasicMovieInformation(movie.getId(), movie.getTitle()));
        }
        movies.sort(comparing(BasicMovieInformation::getTitle));

        // when
        var page1 = movieRepositoryAdapter.search(new BasicMovieQuery("m", new Pagination(10, 1)));
        var page3 = movieRepositoryAdapter.search(new BasicMovieQuery("m", new Pagination(10, 3)));

        // then
        assertThat(page1).isEqualTo(
                new PaginatedSearchResults<>(
                        movies.subList(0, 10),
                        new Pagination(10, 1), (long) n, 5L
                )
        );
        assertThat(page3).isEqualTo(
                new PaginatedSearchResults<>(
                        movies.subList(20, 30),
                        new Pagination(10, 3), (long) n, 5L
                )
        );
    }
}
