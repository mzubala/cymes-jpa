package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.reservation.Reservation;

import java.util.UUID;

public interface ReservationRepository {
    void save(Reservation reservation);
    Reservation get(UUID reservationId);
}
