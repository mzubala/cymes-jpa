package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.CinemaHall;
import pl.com.bottega.cymes.domain.model.CinemaHall.Row;
import pl.com.bottega.cymes.domain.model.commands.CreateCinemaCommand;
import pl.com.bottega.cymes.domain.ports.AdminService;
import pl.com.bottega.cymes.domain.ports.CinemaHallRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import static pl.com.bottega.cymes.domain.model.CinemaHall.of;
import static pl.com.bottega.cymes.domain.ports.CinemaRepository.CinemaExistsException;

@RestController
@RequestMapping("/cinemas")
public class CinemasResource {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @PostMapping
    public void create(@RequestBody CreateCinemaRequest request) {
        adminService.createCinema(new CreateCinemaCommand(UUID.fromString(request.id), request.city, request.name));
    }

    @GetMapping
    public List<CinemaResponse> all() {
        return adminService.getCinemas().stream().map(CinemaResponse::new).collect(Collectors.toList());
    }

    @PutMapping("/{cinemaId}/halls")
    public void createCinemaHall(@PathVariable String cinemaId, @RequestBody CreateCinemaHallRequest createCinemaHallRequest) {
        cinemaHallRepository.save(createCinemaHallRequest.toCinemaHall(cinemaId));
    }

    @GetMapping("/{cinemaId}/halls")
    public List<String> getCinemaHalls(@PathVariable String cinemaId, CreateCinemaHallRequest createCinemaHallRequest) {
        return cinemaHallRepository.getCinemaHallNumbers(UUID.fromString(cinemaId));
    }

    @GetMapping("/{cinemaId}/halls/{hallNumber}")
    public CinemaHallResponse getCinemaHall(@PathVariable String cinemaId, @PathVariable String hallNumber) {
        return new CinemaHallResponse(cinemaHallRepository.get(new CinemaHallId(UUID.fromString(cinemaId), hallNumber)));
    }

    @ExceptionHandler
    public ResponseEntity<Errors> handleCinemaExistsException(CinemaExistsException exception) {
        return new ResponseEntity<>(Errors.of(exception), HttpStatus.CONFLICT);
    }

    @Data
    @Builder
    public static class CreateCinemaRequest {
        private String id;
        private String name;
        private String city;
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCinemaHallRequest {
        private String number;
        private List<String> rows;

        public CinemaHall toCinemaHall(String cinemaId) {
            return of(new CinemaHallId(UUID.fromString(cinemaId), number), rows);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CinemaHallResponse {
        List<String> rows;

        CinemaHallResponse(CinemaHall cinemaHall) {
            rows = cinemaHall.getRows().stream().map(Row::getLayout).collect(Collectors.toList());
        }
    }
}
