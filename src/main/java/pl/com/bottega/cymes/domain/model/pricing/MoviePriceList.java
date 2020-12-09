package pl.com.bottega.cymes.domain.model.pricing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class MoviePriceList {
    private final UUID movieId;
    private PriceList priceList;
}
