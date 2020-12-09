package pl.com.bottega.cymes.domain.model.reservation;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.domain.model.examples.PriceListExample;
import pl.com.bottega.cymes.domain.model.examples.ReservationExample;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.model.reservation.TicketsReceipt.TicketsReceiptItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationTest {

    @Test
    public void doesNotAllowToSelectTicketsWhichAreNotOnThePriceList() {
        var reservation = ReservationExample.builder()
                .priceList(new PriceListExample().with(TicketKind.REGULAR, Money.of(BigDecimal.TEN)).with(TicketKind.FAMILY, Money.of(BigDecimal.ONE)))
                .build().toModel();

        assertThatThrownBy(() -> {
            var ticketsSelection = new HashMap<TicketKind, Integer>();
            ticketsSelection.put(TicketKind.REGULAR, 1);
            ticketsSelection.put(TicketKind.SENIOR, 4);
            reservation.selectTickets(ticketsSelection);
        }).isInstanceOf(Reservation.InvalidTicketSelection.class);
    }

    @Test
    public void returnsCorrectReceipt() {
        var reservation = ReservationExample.builder()
                .priceList(new PriceListExample().with(TicketKind.REGULAR, Money.of(15.0)).with(TicketKind.FAMILY, Money.of(10.0)))
                .build().toModel();
        var ticketsSelection = new HashMap<TicketKind, Integer>();
        ticketsSelection.put(TicketKind.REGULAR, 1);
        ticketsSelection.put(TicketKind.FAMILY, 4);

        reservation.selectTickets(ticketsSelection);
        var receipt = reservation.getReceipt();

        assertThat(receipt.getItems()).containsAll(
                List.of(
                        new TicketsReceiptItem(TicketKind.REGULAR, 1, Money.of(15.0)),
                        new TicketsReceiptItem(TicketKind.FAMILY, 4, Money.of(10.0))
                )
        );
    }

}
