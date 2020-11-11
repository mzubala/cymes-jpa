package pl.com.bottega.cymes.client;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CinemaResponse;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaRequest;
import pl.com.bottega.cymes.adapters.rest.PaginatedSearchResultsResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import static pl.com.bottega.cymes.adapters.rest.MoviesResource.BasicMovieInformationResponse;
import static pl.com.bottega.cymes.adapters.rest.MoviesResource.CreateMovieRequest;

@AllArgsConstructor
public class CymesClient {
    private WebTestClient webClient;

    public void createCinema(CreateCinemaRequest createCinemaRequest) {
        var response = tryCreatingCinema(createCinemaRequest);
        response.expectStatus().is2xxSuccessful();
    }

    public ResponseSpec tryCreatingCinema(CreateCinemaRequest createCinemaRequest) {
        return webClient.post().uri("/cinemas").bodyValue(createCinemaRequest).exchange();
    }

    public List<CinemaResponse> getCinemas() {
        var response = webClient.get().uri("/cinemas").exchange();
        response.expectStatus().is2xxSuccessful();
        return response.returnResult(CinemaResponse.class).getResponseBody().toStream().collect(Collectors.toList());
    }

    public void createMovie(CreateMovieRequest createMovieRequest) {
        var response = webClient.post().uri("/movies").bodyValue(createMovieRequest).exchange();
        response.expectStatus().is2xxSuccessful();
    }

    public PaginatedSearchResultsResponse<BasicMovieInformationResponse> getMovies() {
        var response = webClient.get().uri("/movies").exchange();
        response.expectStatus().is2xxSuccessful();
        return response.returnResult(GetMoviesResponse.class).getResponseBody().blockFirst();
    }

    static class GetMoviesResponse extends PaginatedSearchResultsResponse<BasicMovieInformationResponse> {}
}
