package pl.com.bottega.cymes.adapters.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.Show;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Component
public class ShowRepositoryAdapter implements ShowRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Show show) {
        entityManager.persist(new ShowEntity(
                show.getId(),
                entityManager.getReference(CinemaHallEntity.class, new CinemaHallPK(show.getCinemaHallId())),
                entityManager.getReference(MovieEntity.class, show.getMovieId()),
                show.getStartAt(), show.getEndAt()
        ));
    }
}

@Entity(name = "Show")
@Table(name = "shows")
@AllArgsConstructor
@NoArgsConstructor
class ShowEntity {
    @Id
    private UUID id;

    @ManyToOne
    private CinemaHallEntity cinemaHall;

    @ManyToOne
    private MovieEntity movie;

    private Instant startAt;

    private Instant endAt;
}

