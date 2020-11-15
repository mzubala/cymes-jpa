package pl.com.bottega.cymes.adapters.repository;

import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.com.bottega.cymes.domain.model.CinemaHall.*;

@Component
public class CinemaHallRepositoryAdapter implements CinemaHallRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SpringDataCinemaHallRepository springDataCinemaHallRepository;

    @Override
    @Transactional
    public void save(CinemaHall cinemaHall) {
        springDataCinemaHallRepository.save(new CinemaHallEntity(cinemaHall, em.getReference(CinemaEntity.class, cinemaHall.getId().getCinemaId())));
    }

    @Override
    public List<String> getCinemaHallNumbers(UUID fromString) {
        return springDataCinemaHallRepository.findByCinemaId(fromString);
    }

    @Override
    public CinemaHall get(CinemaHallId id) {
        return springDataCinemaHallRepository.findById(new CinemaHallPK(id)).map(CinemaHallEntity::toDomain)
                .orElseThrow(() -> new AggregateNotFoundException(CinemaHall.class, id));
    }
}

@Entity(name = "CinemaHall")
@Table(name = "cinema_halls")
class CinemaHallEntity {

    @EmbeddedId
    private CinemaHallPK id;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @OrderColumn(name = "number")
    private List<RowEntity> rows;

    @ManyToOne
    @MapsId("cinemaId")
    private CinemaEntity cinema;

    protected CinemaHallEntity() {}

    CinemaHallEntity(CinemaHall cinemaHall, CinemaEntity cinema) {
        this.id = new CinemaHallPK(cinemaHall.getId());
        this.rows = cinemaHall.getRows().stream().map(RowEntity::new).collect(Collectors.toList());
        this.cinema = cinema;
    }

    public CinemaHall toDomain() {
        return new CinemaHall(id.toDomain(), rows.stream().map(RowEntity::toDomain).collect(Collectors.toList()));
    }
}

@Entity(name = "Row")
@Table(name = "rows")
class RowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String layout;

    RowEntity(Row row) {
        this.layout = row.getLayout();
    }

    protected RowEntity() {}

    public Row toDomain() {
        return new Row(layout);
    }
}

@Embeddable
@EqualsAndHashCode
class CinemaHallPK implements Serializable {
    private UUID cinemaId;
    private String number;

    CinemaHallPK(CinemaHallId id) {
        this.cinemaId = id.getCinemaId();
        this.number = id.getNumber();
    }

    protected CinemaHallPK() {
    }

    public CinemaHallId toDomain() {
        return new CinemaHallId(cinemaId, number);
    }
}

interface SpringDataCinemaHallRepository extends Repository<CinemaHallEntity, CinemaHallPK> {
    void save(CinemaHallEntity cinemaHall);

    @Query(value = "SELECT ch.id.number FROM CinemaHall ch WHERE ch.id.cinemaId = :cinemaId")
    List<String> findByCinemaId(UUID cinemaId);

    Optional<CinemaHallEntity> findById(CinemaHallPK id);
}