package pl.com.bottega.cymes.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.cymes.adapters.repository.LockingShowScheduler;
import pl.com.bottega.cymes.domain.model.pricing.MovieBasedShowPricesCalculator;
import pl.com.bottega.cymes.domain.model.pricing.ShowPricesCalculator;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.MoviePriceListRepository;
import pl.com.bottega.cymes.domain.ports.ReservationRepository;
import pl.com.bottega.cymes.domain.ports.ReservationSaga;
import pl.com.bottega.cymes.domain.ports.impl.DefaultReservationSaga;
import pl.com.bottega.cymes.domain.ports.impl.DefaultShowScheduler;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.ShowRepository;
import pl.com.bottega.cymes.domain.ports.ShowScheduler;

import javax.persistence.EntityManager;

@Configuration
public class DomainConfig {
    @Bean
    public ShowScheduler showScheduler(CinemaHallRepository cinemaHallRepository, MovieRepository movieRepository,
                                       ShowRepository showRepository, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        return new LockingShowScheduler(
                new DefaultShowScheduler(cinemaHallRepository, movieRepository, showRepository),
                entityManager, transactionTemplate
        );
    }

    @Bean
    public ReservationSaga reservationSaga(ReservationRepository reservationRepository, ShowRepository showRepository, ShowPricesCalculator showPricesCalculator) {
        return new DefaultReservationSaga(reservationRepository, showRepository, showPricesCalculator);
    }

    @Bean
    public ShowPricesCalculator pricesCalculator(MoviePriceListRepository moviePriceListRepository) {
        return new MovieBasedShowPricesCalculator(moviePriceListRepository);
    }
}
