package pl.com.bottega.cymes.domain.model.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.model.reservation.TicketsReceipt.TicketsReceiptItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Reservation {
    private final UUID id;
    private final UUID showId;
    private final PriceList priceList;
    private Map<TicketKind, Integer> ticketCounts;

    public void selectTickets(Map<TicketKind, Integer> counts) throws InvalidTicketSelection {
        if (!priceList.containsAll(counts.keySet())) {
            throw new InvalidTicketSelection();
        }
        this.ticketCounts = new HashMap<>(counts);
    }

    public TicketsReceipt getReceipt() {
        return new TicketsReceipt(ticketCounts.entrySet().stream()
                .map((entry) -> new TicketsReceiptItem(
                        entry.getKey(), entry.getValue(),
                        priceList.priceOf(entry.getKey()))
                ).collect(Collectors.toList()));
    }

    public static class InvalidTicketSelection extends RuntimeException {
    }
}
