package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.commands.CreateReservationCommand;
import pl.com.bottega.cymes.domain.model.commands.SelectTicketsCommand;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.model.reservation.TicketsReceipt;
import pl.com.bottega.cymes.domain.model.reservation.TicketsReceipt.TicketsReceiptItem;
import pl.com.bottega.cymes.domain.model.reservation.Reservation;
import pl.com.bottega.cymes.domain.ports.ReservationRepository;
import pl.com.bottega.cymes.domain.ports.ReservationSaga;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/reservations")
public class ReservationResource {

    @Autowired
    private ReservationSaga reservationSaga;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping
    public void create(@RequestBody CreateReservationRequest request) {
        reservationSaga.createReservation(request.toCommand());
    }

    @GetMapping("/{id}/price-list")
    public PriceListResponse getPriceList(@PathVariable String id) {
        var reservation = reservationRepository.get(UUID.fromString(id));
        return new PriceListResponse(reservation.getPriceList());
    }

    @PutMapping("/{id}/tickets")
    public void selectTickets(@PathVariable String id, @RequestBody SelectTicketsRequest request) {
        reservationSaga.selectTickets(request.toCommand(UUID.fromString(id)));
    }

    @GetMapping("/{id}")
    public GetReservationResponse getReservation(@PathVariable String id) {
        var reservation = reservationRepository.get(UUID.fromString(id));
        return new GetReservationResponse(reservation);
    }

    @Data
    public static class CreateReservationRequest {
        @NonNull
        String reservationId;
        @NonNull
        String showId;

        public CreateReservationCommand toCommand() {
            return new CreateReservationCommand(UUID.fromString(reservationId), UUID.fromString(showId));
        }
    }

    @Data
    @NoArgsConstructor
    public static class PriceListResponse {
        @NonNull
        Map<TicketKind, BigDecimal> priceList;

        public PriceListResponse(PriceList priceList) {
            this.priceList = priceList.getPrices().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toBigDecimal()));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectTicketsRequest {
        @NonNull
        Map<TicketKind, Integer> counts;

        public SelectTicketsCommand toCommand(UUID reservationId) {
            return new SelectTicketsCommand(reservationId, counts);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReservationResponse {
        private String id;
        private TicketsReceiptResponse receipt;

        public GetReservationResponse(Reservation reservation) {
            this(reservation.getId().toString(), new TicketsReceiptResponse(reservation.getReceipt()));
        }
    }

    @Data
    @NoArgsConstructor
    public static class TicketsReceiptResponse {
        private List<TicketsReceiptItemResponse> items;
        private BigDecimal total;

        public TicketsReceiptResponse(TicketsReceipt receipt) {
            this.items = receipt.getItems().stream().map(TicketsReceiptItemResponse::new).collect(toList());
            total = receipt.getTotal().getAmount();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketsReceiptItemResponse {
        private TicketKind ticketKind;
        private Integer count;
        private BigDecimal price;
        private BigDecimal total;

        TicketsReceiptItemResponse(TicketsReceiptItem item) {
            ticketKind = item.getTicketKind();
            count = item.getCount();
            price = item.getPrice().getAmount();
            total = item.getTotal().getAmount();
        }
    }
}
