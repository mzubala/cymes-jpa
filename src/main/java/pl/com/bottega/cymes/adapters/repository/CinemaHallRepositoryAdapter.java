package pl.com.bottega.cymes.adapters.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import pl.com.bottega.cymes.domain.model.CinemaHall.Row;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CinemaHallRepositoryAdapter implements CinemaHallRepository {

    @Autowired
    private SpringDataCinemaHallRepository springDataCinemaHallRepository;

    @Override
    public void save(CinemaHall cinemaHall) {
        springDataCinemaHallRepository.save(new CinemaHallEntity(cinemaHall));
    }

    @Override
    public List<String> getCinemaHallNumbers(UUID fromString) {
        return springDataCinemaHallRepository.findByCinemaId(fromString);
    }

    @Override
    public CinemaHall get(CinemaHallId id) {
        return springDataCinemaHallRepository.findById(new CinemaHallPK(id)).toDomain();
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

    protected CinemaHallEntity() {}

    CinemaHallEntity(CinemaHall cinemaHall) {
        this.id = new CinemaHallPK(cinemaHall.getId());
        this.rows = cinemaHall.getRows().stream().map(RowEntity::new).collect(Collectors.toList());
    }

    public CinemaHall toDomain() {
        return new CinemaHall(id.toDomain(), rows.stream().map(RowEntity::toDomain).collect(Collectors.toList()));
    }
}

@Entity(name = "Row")
@Table(name = "rows")
class RowEntity {
    @Id
    @GeneratedValue
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

    CinemaHallEntity findById(CinemaHallPK id);
}