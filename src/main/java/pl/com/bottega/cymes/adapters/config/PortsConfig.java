package pl.com.bottega.cymes.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.cymes.adapters.repository.MovieRepositoryAdapter;
import pl.com.bottega.cymes.domain.ports.AdminService;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;

@Configuration
public class PortsConfig {

    @Bean
    public AdminService adminService(CinemaRepository cinemaRepository, MovieRepositoryAdapter movieRepositoryAdapter) {
        return new AdminService(cinemaRepository, movieRepositoryAdapter);
    }
}
