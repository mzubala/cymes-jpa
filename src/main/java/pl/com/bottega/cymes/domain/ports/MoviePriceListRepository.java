package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.pricing.MoviePriceList;

import java.util.UUID;

public interface MoviePriceListRepository {
    void save(MoviePriceList priceList);
    MoviePriceList get(UUID movieId) throws AggregateNotFoundException;
}
