package pl.com.bottega.cymes.adapters.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.model.reservation.Reservation;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;
import pl.com.bottega.cymes.domain.ports.ReservationRepository;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ReservationRepositoryAdapter implements ReservationRepository {

    @Autowired
    private SpringDataReservationRepository springDataReservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Reservation reservation) {
        springDataReservationRepository.save(new ReservationEntity(reservation, entityManager.getReference(ShowEntity.class, reservation.getShowId())));
    }

    @Override
    public Reservation get(UUID reservationId) {
        return springDataReservationRepository.findById(reservationId).map(ReservationEntity::toDomain)
                .orElseThrow(() -> new AggregateNotFoundException(Reservation.class, reservationId));
    }
}

@Entity(name = "Reservation")
@Table(name = "reservations")
class ReservationEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShowEntity show;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_price_list_items",
            joinColumns = {@JoinColumn(name = "reservationId", referencedColumnName = "id")})
    @MapKeyColumn(name = "ticket_kind")
    @Column(name = "price")
    private Map<TicketKind, BigDecimal> prices;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_ticket_counts",
            joinColumns = {@JoinColumn(name = "reservationId", referencedColumnName = "id")})
    @MapKeyColumn(name = "ticket_kind")
    @Column(name = "tickets_count")
    private Map<TicketKind, Integer> ticketCounts;

    protected ReservationEntity() {
    }

    ReservationEntity(Reservation reservation, ShowEntity show) {
        this.id = reservation.getId();
        this.prices = reservation.getPriceList().getPrices().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toBigDecimal()));
        this.show = show;
        this.ticketCounts = reservation.getTicketCounts();
    }

    public Reservation toDomain() {
        return new Reservation(id, show.getId(),
                new PriceList(prices.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> Money.of(e.getValue())))),
                ticketCounts
        );
    }
}

interface SpringDataReservationRepository extends Repository<ReservationEntity, UUID> {
    void save(ReservationEntity reservationEntity);

    Optional<ReservationEntity> findById(UUID id);
}

