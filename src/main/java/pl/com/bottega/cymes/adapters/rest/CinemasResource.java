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
import pl.com.bottega.cymes.domain.ports.AdminService;
import pl.com.bottega.cymes.domain.ports.AdminService.CreateCinemaCommand;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cinemas")
public class CinemasResource {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public void create(@RequestBody CreateCinemaRequest request) {
        adminService.createCinema(new CreateCinemaCommand(UUID.fromString(request.id), request.city, request.name));
    }

    @GetMapping
    public List<CinemaResponse> all() {
        return adminService.getAll().stream().map(CinemaResponse::new).collect(Collectors.toList());
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
}