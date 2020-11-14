package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.ports.CinemaRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cinemas")
public class CinemasResource {

    @Autowired
    private CinemaRepository cinemaRepository;

    @PostMapping
    public void create(@RequestBody CreateCinemaRequest request) {
        cinemaRepository.save(request.toDomain());
    }

    @GetMapping
    public List<CinemaResponse> all() {
        return cinemaRepository.getAll().stream().map(CinemaResponse::new).collect(Collectors.toList());
    }

    @Data
    @Builder
    public static class CreateCinemaRequest {
        private String id;
        private String name;
        private String city;

        public Cinema toDomain() {
            return new Cinema(UUID.fromString(id), city, name);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CinemaResponse {
        private String id;
        private String name;
        private String city;

        CinemaResponse(Cinema cinema) {
            this.id = cinema.getId().toString();
            this.name = cinema.getName();
            this.city = cinema.getCity();
        }
    }
}
