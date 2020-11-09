package pl.com.bottega.cymes.domain.ports;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import pl.com.bottega.cymes.domain.model.Cinema;

import java.util.List;
import java.util.UUID;

import static pl.com.bottega.cymes.domain.ports.CinemaRepository.*;

@AllArgsConstructor
public class AdminService {

    private final CinemaRepository cinemaRepository;

    public void createCinema(CreateCinemaCommand command) throws CinemaExistsException {
        var cinema = new Cinema(command.id, withoutSpaces(command.city), withoutSpaces(command.name));
        cinemaRepository.save(cinema);
    }

    public List<Cinema> getCinemas() {
        return cinemaRepository.getAll();
    }

    private String withoutSpaces(String input) {
        return StringUtils.join(input.trim().split("\\s+"), " ");
    }

    @Value
    public static class CreateCinemaCommand {
        @NonNull
        UUID id;
        @NonNull
        String city;
        @NonNull
        String name;
    }
}
