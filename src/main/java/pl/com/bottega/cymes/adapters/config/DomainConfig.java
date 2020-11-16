package pl.com.bottega.cymes.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.cymes.adapters.repository.LockingShowScheduler;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.DefaultShowScheduler;
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
}
