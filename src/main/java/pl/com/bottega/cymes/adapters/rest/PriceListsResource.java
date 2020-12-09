package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.MoviePriceList;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.ports.MoviePriceListRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/price-lists")
public class PriceListsResource {

    @Autowired
    private MoviePriceListRepository moviePriceListRepository;

    @PostMapping("/movies")
    public void saveMoviePriceList(@RequestBody SaveMoviePriceListRequest saveMoviePriceListRequest) {
        moviePriceListRepository.save(saveMoviePriceListRequest.toDomain());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaveMoviePriceListRequest {
        private String movieId;
        private Map<TicketKind, BigDecimal> prices;

        MoviePriceList toDomain() {
            var priceList = new PriceList(prices.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Money.of(e.getValue()))));
            return new MoviePriceList(UUID.fromString(movieId), priceList);
        }
    }
}
