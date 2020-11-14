package pl.com.bottega.cymes.adapters.rest;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.adapters.SpringAdapterTest;

import java.util.List;
import java.util.UUID;

public class CinemasResourceTest extends SpringAdapterTest {

    @Test
    public void cannotCreateCinemaHallIfCinemaDoesNotExist() {
        var wrongCinemaId = UUID.randomUUID();
        cymesClient.tryCreatingCinemaHall(wrongCinemaId.toString(), new CinemasResource.CreateCinemaHallRequest("1", List.of(
                "XXX"
        ))).expectStatus().isNotFound();
    }
}
