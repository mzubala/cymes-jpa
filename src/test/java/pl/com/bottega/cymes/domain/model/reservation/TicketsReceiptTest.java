package pl.com.bottega.cymes.domain.model.reservation;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.model.reservation.TicketsReceipt.TicketsReceiptItem;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TicketsReceiptTest {
    @Test
    public void returnsCorrectTotal() {
        TicketsReceipt ticketsReceipt = new TicketsReceipt(List.of(
                new TicketsReceiptItem(TicketKind.REGULAR, 1, Money.of(20)),
                new TicketsReceiptItem(TicketKind.FAMILY, 4, Money.of(10)),
                new TicketsReceiptItem(TicketKind.SENIOR, 1, Money.of(5))
        ));

        assertThat(ticketsReceipt.getTotal()).isEqualTo(Money.of(65));
    }

    @Test
    public void doesNotAllowDuplicateTicketKinds() {
        assertThatThrownBy(() -> new TicketsReceipt(List.of(
                new TicketsReceiptItem(TicketKind.REGULAR, 1, Money.of(20)),
                new TicketsReceiptItem(TicketKind.REGULAR, 4, Money.of(10))
        ))).isInstanceOf(IllegalArgumentException.class);
    }
}
