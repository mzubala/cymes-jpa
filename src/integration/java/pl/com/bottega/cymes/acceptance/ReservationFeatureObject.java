package pl.com.bottega.cymes.acceptance;

import pl.com.bottega.cymes.client.CymesClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static pl.com.bottega.cymes.adapters.rest.ReservationResource.*;
import static pl.com.bottega.cymes.adapters.rest.ReservationResource.CreateReservationRequest;
import static pl.com.bottega.cymes.adapters.rest.ShowsResource.SearchShowsRequest;
import static pl.com.bottega.cymes.adapters.rest.ShowsResource.SearchedShowResponse;

public class ReservationFeatureObject {
    private final CymesClient cymesClient;
    private final UUID reservationId = UUID.randomUUID();
    private SearchedShowResponse selectedShow;

    public ReservationFeatureObject(CymesClient cymesClient) {
        this.cymesClient = cymesClient;
    }

    public void selectShow(String movieTitle, String city, String cinemaName, String showStartAt) {
        var movieId = cymesClient.getMovieId(movieTitle);
        var cinemaId = cymesClient.getCinemaId(cinemaName, city);
        var showStart = Instant.parse(showStartAt);
        SearchShowsRequest searchShowsRequest = SearchShowsRequest.builder().cinemaId(cinemaId).movieId(movieId).day(LocalDate.ofInstant(showStart, ZoneId.systemDefault())).build();
        this.selectedShow = cymesClient.searchShows(searchShowsRequest).stream()
                .filter((show) -> show.getStartTime().equals(showStart))
                .findFirst().orElseThrow();
        cymesClient.createReservation(new CreateReservationRequest(reservationId.toString(), selectedShow.getShowId()));
    }

    public PriceListResponse getPriceList() {
        return cymesClient.getReservationPriceList(reservationId);
    }

    public void selectTickets(SelectTicketsRequest request) {
        cymesClient.selectTickets(reservationId, request);
    }

    public GetReservationResponse getReservation() {
        return cymesClient.getReservation(reservationId);
    }
}
