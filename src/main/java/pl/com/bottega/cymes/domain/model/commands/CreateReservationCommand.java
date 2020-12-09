package pl.com.bottega.cymes.domain.model.commands;

import lombok.Value;

import java.util.UUID;

@Value
public class CreateReservationCommand {
    UUID reservationId;
    UUID showId;
}
