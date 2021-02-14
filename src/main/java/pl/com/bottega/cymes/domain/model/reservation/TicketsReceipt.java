package pl.com.bottega.cymes.domain.model.reservation;

import lombok.Value;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.util.LinkedList;
import java.util.List;

@Value
public class TicketsReceipt {

    List<TicketsReceiptItem> items;

    public TicketsReceipt(List<TicketsReceiptItem> items) {
        if(uniqueItemsCount(items) != items.size()) {
            throw new IllegalArgumentException("Duplicate receipt items");
        }
        this.items = new LinkedList<>(items);
    }

    public Money getTotal() {
        return items.stream().map(TicketsReceiptItem::getTotal).reduce(Money.ZERO, Money::add);
    }

    private long uniqueItemsCount(List<TicketsReceiptItem> items) {
        return items.stream().map(TicketsReceiptItem::getTicketKind).distinct().count();
    }

    @Value
    public static class TicketsReceiptItem {
        TicketKind ticketKind;
        Integer count;
        Money price;

        public Money getTotal() {
            return price.times(count);
        }
    }
}
