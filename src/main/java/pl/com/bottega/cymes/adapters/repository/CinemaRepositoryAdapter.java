package pl.com.bottega.cymes.adapters.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class CinemaRepositoryAdapter implements CinemaRepository {

    private SpringDataCinemaRepository springDataCinemaRepository;

    public void save(Cinema cinema) {
        try {
            springDataCinemaRepository.save(new CinemaEntity(cinema));
        } catch (DataIntegrityViolationException ex) {
            throw new CinemaExistsException();
        }
    }

    @Override
    public List<Cinema> getAll() {
        return springDataCinemaRepository.findAll(Sort.by("city", "name")).map(CinemaEntity::toDomain).collect(Collectors.toList());
    }
}

@Table(name = "cinemas",
        indexes = {
                @Index(columnList = "city, name", unique = true)
        }
)
@Entity(name = "Cinema")
@Data
class CinemaEntity {

    @Id
    private UUID id;

    private String name;
    private String city;

    CinemaEntity(Cinema cinema) {
        this.id = cinema.getId();
        this.city = cinema.getCity();
        this.name = cinema.getName();
    }

    protected CinemaEntity() {
    }

    Cinema toDomain() {
        return new Cinema(id, city, name);
    }
}

interface SpringDataCinemaRepository extends Repository<CinemaEntity, Long> {
    void save(CinemaEntity cinemaEntity);
    Stream<CinemaEntity> findAll(Sort sort);
}
