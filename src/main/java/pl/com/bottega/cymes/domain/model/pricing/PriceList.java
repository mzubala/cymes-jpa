package pl.com.bottega.cymes.domain.model.pricing;

import lombok.Value;

import javax.management.Query;
import java.util.Map;
import java.util.Set;

@Value
public class PriceList {
    Map<TicketKind, Money> prices;

    public boolean containsAll(Set<TicketKind> ticketKinds) {
        return prices.keySet().containsAll(ticketKinds);
    }

    public Money priceOf(TicketKind ticketKind) {
        if(!prices.containsKey(ticketKind)) {
            throw new IllegalArgumentException();
        }
        return prices.get(ticketKind);
    }
}
