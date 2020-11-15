package pl.com.bottega.cymes.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;
import pl.com.bottega.cymes.domain.ports.DefaultShowScheduler;
import pl.com.bottega.cymes.domain.ports.MovieRepository;
import pl.com.bottega.cymes.domain.ports.ShowRepository;
import pl.com.bottega.cymes.domain.ports.ShowScheduler;

@Configuration
public class DomainConfig {
    @Bean
    public ShowScheduler showScheduler(CinemaHallRepository cinemaHallRepository, MovieRepository movieRepository, ShowRepository showRepository) {
        return new DefaultShowScheduler(cinemaHallRepository, movieRepository, showRepository);
    }
}
