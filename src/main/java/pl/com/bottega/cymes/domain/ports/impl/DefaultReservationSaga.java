package pl.com.bottega.cymes.domain.ports.impl;

import lombok.AllArgsConstructor;
import pl.com.bottega.cymes.domain.model.commands.CreateReservationCommand;
import pl.com.bottega.cymes.domain.model.commands.SelectTicketsCommand;
import pl.com.bottega.cymes.domain.model.pricing.ShowPricesCalculator;
import pl.com.bottega.cymes.domain.model.reservation.Reservation;
import pl.com.bottega.cymes.domain.ports.ReservationRepository;
import pl.com.bottega.cymes.domain.ports.ReservationSaga;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

@AllArgsConstructor
public class DefaultReservationSaga implements ReservationSaga {

    private final ReservationRepository reservationRepository;
    private final ShowRepository showRepository;
    private final ShowPricesCalculator priceCalculator;

    @Override
    public void createReservation(CreateReservationCommand command) {
        var show = showRepository.get(command.getShowId());
        var priceList = priceCalculator.calculate(show);
        var reservation = new Reservation(command.getReservationId(), command.getShowId(), priceList);
        reservationRepository.save(reservation);
    }

    @Override
    public void selectTickets(SelectTicketsCommand selectTicketsCommand) {
        var reservation = reservationRepository.get(selectTicketsCommand.getReservationId());
        reservation.selectTickets(selectTicketsCommand.getCounts());
        reservationRepository.save(reservation);
    }
}
