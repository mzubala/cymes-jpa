package pl.com.bottega.cymes.domain.model.examples;

import lombok.Builder;
import pl.com.bottega.cymes.domain.model.reservation.Reservation;

import java.util.UUID;

@Builder
public class ReservationExample {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Builder.Default
    private UUID showId = UUID.randomUUID();
    @Builder.Default
    private PriceListExample priceList = new PriceListExample();

    public Reservation toModel() {
        return new Reservation(id, showId, priceList.toModel());
    }
}
