package pl.com.bottega.cymes.domain.model.examples;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.MoviePriceList;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor
public class MoviePriceListExample {
    
    private UUID movieId = UUID.randomUUID();
    
    private Map<TicketKind, Money> prices = new HashMap<>();
    
    public MoviePriceListExample withPrice(TicketKind ticketKind, Money price) {
        prices.put(ticketKind, price);
        return this;
    }
    
    public MoviePriceList toDomain() {
        return new MoviePriceList(movieId, new PriceList(prices));
    }
}
