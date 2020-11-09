package pl.com.bottega.cymes.domain.ports;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.ports.AdminService.CreateCinemaCommand;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class AdminServiceTest {

    private CinemaRepository cinemaRepository = mock(CinemaRepository.class);
    private AdminService adminService = new AdminService(cinemaRepository);

    @Test
    public void createsCinema() {
        // when
        UUID id = UUID.randomUUID();
        adminService.createCinema(new CreateCinemaCommand(id, "Lublin", "Plaza"));

        // then
        verify(cinemaRepository, only()).save(new Cinema(id, "Lublin", "Plaza"));
    }

    @Test
    public void escapesExtraSpacesWhenCreatingCinemas() {
        // when
        UUID id = UUID.randomUUID();
        adminService.createCinema(new CreateCinemaCommand(id, "  Zielona   Góra  ", " Złote  Tarasy "));

        // then
        verify(cinemaRepository, only()).save(new Cinema(id, "Zielona Góra", "Złote Tarasy"));
    }
}
