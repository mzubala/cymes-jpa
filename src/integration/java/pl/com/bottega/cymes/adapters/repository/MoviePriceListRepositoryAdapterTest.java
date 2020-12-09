package pl.com.bottega.cymes.adapters.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.bottega.cymes.adapters.SpringAdapterTest;
import pl.com.bottega.cymes.domain.model.examples.MovieExample;
import pl.com.bottega.cymes.domain.model.examples.MoviePriceListExample;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MoviePriceListRepositoryAdapterTest extends SpringAdapterTest {

    @Autowired
    private MovieRepositoryAdapter movieRepositoryAdapter;
    @Autowired
    private MoviePriceListRepositoryAdapter moviePriceListRepositoryAdapter;

    @Test
    public void savesAndReadsMoviePriceList() {
        var movie = MovieExample.builder().build().toDomain();
        var moviePriceList = new MoviePriceListExample().withMovieId(movie.getId())
                .withPrice(TicketKind.FAMILY, Money.of(BigDecimal.valueOf(10))).toDomain();

        movieRepositoryAdapter.save(movie);
        moviePriceListRepositoryAdapter.save(moviePriceList);
        var fetchedPriceList = moviePriceListRepositoryAdapter.get(movie.getId());

        assertThat(fetchedPriceList.getPriceList()).isEqualTo(moviePriceList.getPriceList());
    }
}
