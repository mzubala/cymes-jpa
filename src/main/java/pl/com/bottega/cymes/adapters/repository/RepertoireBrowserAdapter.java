package pl.com.bottega.cymes.adapters.repository;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.queries.ShowsQuery;
import pl.com.bottega.cymes.domain.ports.RepertoireBrowser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class RepertoireBrowserAdapter implements RepertoireBrowser {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SearchedShow> search(ShowsQuery query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchedShow> criteriaQuery = criteriaBuilder.createQuery(SearchedShow.class);
        Root<ShowEntity> show = criteriaQuery.from(ShowEntity.class);
        Join<ShowEntity, MovieEntity> movie = show.join("movie");
        Join<ShowEntity, CinemaHallEntity> cinemaHall = show.join("cinemaHall");
        Join<CinemaHallEntity, Cinema> cinema = cinemaHall.join("cinema");
        criteriaQuery.select(
                criteriaBuilder.construct(SearchedShow.class,
                        show.get("id"),
                        movie.get("title"),
                        cinema.get("name"),
                        cinema.get("city"),
                        cinema.get("id"),
                        show.get("startAt")
                )
        );
        Predicate predicate = criteriaBuilder.between(
                show.get("startAt"),
                query.getDay().atTime(0, 0).toInstant(ZoneOffset.UTC),
                query.getDay().atTime(23, 59).toInstant(ZoneOffset.UTC)
        );
        if (query.getCinemaId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(cinema.get("id"), query.getCinemaId()));
        }
        if (query.getMovieId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(movie.get("id"), query.getMovieId()));
        }
        if(query.getCity() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(cinema.get("city"), query.getCity()));
        }
        criteriaQuery.where(predicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(show.get("startAt")), criteriaBuilder.asc(cinema.get("name")));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
