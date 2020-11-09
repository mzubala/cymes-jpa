package pl.com.bottega.cymes.client;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CinemaResponse;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaRequest;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CymesClient {
    private WebTestClient webClient;

    public void createCinema(CreateCinemaRequest createCinemaRequest) {
        var response = webClient.post().uri("/cinemas").bodyValue(createCinemaRequest).exchange();
        response.expectStatus().is2xxSuccessful();
    }

    public List<CinemaResponse> getCinemas() {
        var response = webClient.get().uri("/cinemas").exchange();
        response.expectStatus().is2xxSuccessful();
        return response.returnResult(CinemaResponse.class).getResponseBody().toStream().collect(Collectors.toList());
    }
}
