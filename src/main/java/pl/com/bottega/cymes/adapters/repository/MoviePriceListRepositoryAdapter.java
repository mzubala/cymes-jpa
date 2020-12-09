package pl.com.bottega.cymes.adapters.repository;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.bottega.cymes.domain.model.pricing.Money;
import pl.com.bottega.cymes.domain.model.pricing.MoviePriceList;
import pl.com.bottega.cymes.domain.model.pricing.PriceList;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;
import pl.com.bottega.cymes.domain.ports.AggregateNotFoundException;
import pl.com.bottega.cymes.domain.ports.MoviePriceListRepository;

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
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MoviePriceListRepositoryAdapter implements MoviePriceListRepository {

    @Autowired
    private SpringDataMoviePriceListRepository springDataMoviePriceListRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(MoviePriceList priceList) {
        springDataMoviePriceListRepository.save(new MoviePriceListEntity(priceList, entityManager.getReference(MovieEntity.class, priceList.getMovieId())));
    }

    @Override
    public MoviePriceList get(UUID movieId) throws AggregateNotFoundException {
        return springDataMoviePriceListRepository.findByMovieId(movieId).map(MoviePriceListEntity::toDomain)
                .orElseThrow(() -> new AggregateNotFoundException(MoviePriceList.class, movieId));
    }
}

interface SpringDataMoviePriceListRepository extends Repository<MoviePriceListEntity, UUID> {
    void save(MoviePriceListEntity moviePriceListEntity);

    Optional<MoviePriceListEntity> findByMovieId(UUID movieId);
}

@Entity(name = "MoviePriceList")
@Table(name = "movie_price_lists")
@NoArgsConstructor
class MoviePriceListEntity {

    @Id
    @Column(name = "movieId")
    private UUID movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movieId")
    private MovieEntity movie;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_price_list_items",
            joinColumns = {@JoinColumn(name = "movieId", referencedColumnName = "movieId")})
    @MapKeyColumn(name = "ticket_kind")
    @Column(name = "price")
    private Map<TicketKind, BigDecimal> prices;

    public MoviePriceListEntity(MoviePriceList moviePriceList, MovieEntity movieEntity) {
        this.movieId = moviePriceList.getMovieId();
        this.movie = movieEntity;
        this.prices = moviePriceList.getPriceList().getPrices().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toBigDecimal()));
    }

    MoviePriceList toDomain() {
        return new MoviePriceList(movieId, new PriceList(prices.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Money.of(e.getValue())))));
    }
}

