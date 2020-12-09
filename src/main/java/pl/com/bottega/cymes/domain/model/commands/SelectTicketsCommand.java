package pl.com.bottega.cymes.domain.model.commands;

import lombok.Value;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.util.Map;
import java.util.UUID;

@Value
public class SelectTicketsCommand {
    UUID reservationId;
    Map<TicketKind, Integer> counts;
}
