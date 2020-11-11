package pl.com.bottega.cymes.domain.ports;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.domain.model.Cinema;
import pl.com.bottega.cymes.domain.model.commands.CreateCinemaCommand;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static pl.com.bottega.cymes.domain.ports.CinemaRepository.CinemaExistsException;

public class AdminServiceTest {

    private CinemaRepository cinemaRepository = mock(CinemaRepository.class);
    private MovieRepository movieRepository = mock(MovieRepository.class);
    private AdminService adminService = new AdminService(cinemaRepository, movieRepository);

    @Test
    public void createsCinema() {
        // when
        UUID id = UUID.randomUUID();
        adminService.createCinema(new CreateCinemaCommand(id, "Lublin", "Plaza"));

        // then
        verify(cinemaRepository, only()).save(new Cinema(id, "Lublin", "Plaza"));
    }

    @Test
    public void throwsErrorWhenCinemaAlreadyExists() {
        // given
        Exception exception = new CinemaExistsException();
        doThrow(exception).when(cinemaRepository).save(any(Cinema.class));

        // then
        assertThatThrownBy(() -> {
            adminService.createCinema(new CreateCinemaCommand(UUID.randomUUID(), "Lublin", "Plaza"));
        }).isEqualTo(exception);

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
