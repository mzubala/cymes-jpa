package pl.com.bottega.cymes.domain.model.pricing;

import pl.com.bottega.cymes.domain.model.Show;
import pl.com.bottega.cymes.domain.ports.MoviePriceListRepository;

public class MovieBasedShowPricesCalculator implements ShowPricesCalculator {

    private final MoviePriceListRepository moviePriceListRepository;

    public MovieBasedShowPricesCalculator(MoviePriceListRepository moviePriceListRepository) {
        this.moviePriceListRepository = moviePriceListRepository;
    }

    @Override
    public PriceList calculate(Show show) {
        return moviePriceListRepository.get(show.getMovieId()).getPriceList();
    }
}
