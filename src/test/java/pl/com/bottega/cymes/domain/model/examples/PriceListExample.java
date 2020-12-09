package pl.com.bottega.cymes.domain.model.examples;

import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.util.HashMap;
import java.util.Map;

public class PriceListExample {

    Map<TicketKind, Money> prices = new HashMap<>();

    public PriceListExample with(TicketKind ticketKind, Money price) {
        prices.put(ticketKind, price);
        return this;
    }

    public PriceList toModel() {
        return new PriceList(prices);
    }
}
