package pl.com.bottega.cymes.adapters.repository;

import lombok.Data;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.domain.model.Genere;
import pl.com.bottega.cymes.domain.model.Movie;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.PaginatedSearchResults;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;
import pl.com.bottega.cymes.domain.ports.MovieRepository;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class MovieRepositoryAdapter implements MovieRepository {

    @Autowired
    private SpringDataMovieEntityRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Movie movie) {
        repository.save(new MovieEntity(movie));
    }

    @Override
    public Movie get(UUID id) {
        return repository.findById(id).map(MovieEntity::toDomain).orElseThrow(() -> new AggregateNotFoundException(Movie.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedSearchResults<BasicMovieInformation> search(BasicMovieQuery query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BasicMovieInformation> criteriaQuery = criteriaBuilder.createQuery(BasicMovieInformation.class);
        Root<MovieEntity> movie = criteriaQuery.from(MovieEntity.class);
        SearchMovieSpecification specification = new SearchMovieSpecification(query);
        criteriaQuery.select(criteriaBuilder.construct(BasicMovieInformation.class, movie.get("id"), movie.get("title")));
        criteriaQuery.where(specification.toPredicate(movie, criteriaQuery, criteriaBuilder));
        criteriaQuery.orderBy(criteriaBuilder.asc(movie.get("title")));
        List<BasicMovieInformation> results = entityManager.createQuery(criteriaQuery)
                .setMaxResults(query.getPagination().getPerPage())
                .setFirstResult((query.getPagination().getPageNumber() - 1) * query.getPagination().getPerPage())
                .getResultList();
        var totalCount = repository.count(specification);
        return new PaginatedSearchResults<BasicMovieInformation>(
                results,
                query.getPagination(),
                totalCount,
                (totalCount / query.getPagination().getPerPage()) + (totalCount % query.getPagination().getPerPage() > 0 ? 1L : 0L)
        );
    }
}

@Entity(name = "Movie")
@Table(name = "movies")
@Data
class MovieEntity {
    @Id
    private UUID id;
    private String title;
    private Integer productionYear;
    private String description;
    private Duration duration;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> actors;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Genere> generes;

    protected MovieEntity() {
    }

    MovieEntity(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.productionYear = movie.getProductionYear();
        this.description = movie.getDescription();
        this.duration = movie.getDuration();
        this.actors = movie.getActors();
        this.generes = movie.getGeneres();
    }

    Movie toDomain() {
        return new Movie(id, title, productionYear, description, duration, actors, generes);
    }

}

@Value
class SearchMovieSpecification implements Specification<MovieEntity> {

    BasicMovieQuery params;

    @Override
    public Predicate toPredicate(Root<MovieEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(params.getPhrase() != null) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + params.getPhrase().toLowerCase() + "%");
        }
        return criteriaBuilder.conjunction();
    }
}

interface SpringDataMovieEntityRepository extends Repository<MovieEntity, UUID>, JpaSpecificationExecutor<MovieEntity> {
    void save(MovieEntity movieEntity);

    Optional<MovieEntity> findById(UUID id);

    Page<BasicMovieInformation> findByTitleLike(String phrase, Pageable pageable);
}
