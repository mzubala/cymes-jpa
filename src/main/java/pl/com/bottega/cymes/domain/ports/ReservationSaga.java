package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.commands.CreateReservationCommand;
import pl.com.bottega.cymes.domain.model.commands.SelectTicketsCommand;

public interface ReservationSaga {
    void createReservation(CreateReservationCommand command);

    void selectTickets(SelectTicketsCommand toCommand);
}
