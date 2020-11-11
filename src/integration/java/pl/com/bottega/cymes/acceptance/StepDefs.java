package pl.com.bottega.cymes.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaRequest;
import pl.com.bottega.cymes.adapters.rest.Errors;
import pl.com.bottega.cymes.adapters.rest.MoviesResource;
import pl.com.bottega.cymes.adapters.rest.PaginatedSearchResultsResponse;
import pl.com.bottega.cymes.client.CymesClient;
import pl.com.bottega.cymes.client.DbClient;
import pl.com.bottega.cymes.domain.model.Genere;

import java.time.Duration;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.adapters.rest.MoviesResource.*;

public class StepDefs extends SpringAcceptanceTest {

    private CymesClient cymesClient;

    @LocalServerPort
    private int port;

    @Autowired
    private DbClient dbClient;

    @Before
    public void setup() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        cymesClient = new CymesClient(webTestClient);
        dbClient.clean();
    }

    @When("network admin creates a cinema {string} in {string}")
    public void network_admin_creates_a_cinema_in(String name, String city) {
        cymesClient.createCinema(CreateCinemaRequest.builder().id(UUID.randomUUID().toString()).city(city).name(name).build());
    }

    @Then("admin cinemas list contains following cinemas:")
    public void admin_cinemas_list_contains_following_cinemas(List<Map<String, String>> cinemasTable) {
        var cinemas = cymesClient.getCinemas();
        assertThat(cinemas.size()).isEqualTo(cinemasTable.size());
        cinemasTable.stream().forEach(cinemaAttributes -> {
            var cinema = cinemas.get(cinemasTable.indexOf(cinemaAttributes));
            assertThat(cinema.getId()).isNotNull();
            assertThat(cinema.getCity()).isEqualTo(cinemaAttributes.get("city"));
            assertThat(cinema.getName()).isEqualTo(cinemaAttributes.get("name"));
        });
    }

    @Then("creating cinema {string} in {string} fails with conflict error")
    public void creating_cinema_in_fails_with_conflict_error(String name, String city) {
        cymesClient.tryCreatingCinema(CreateCinemaRequest.builder().id(UUID.randomUUID().toString()).city(city).name(name).build())
                .expectStatus().isEqualTo(HttpStatus.CONFLICT).expectBody(Errors.class)
                .isEqualTo(Errors.of(new Errors.Error("CinemaExistsError", null)));
    }

    @Given("network admin created movies:")
    @When("network admin creates movies:")
    public void network_admin_creates_movies(List<Map<String, String>> dataTable) {
        dataTable.forEach(row -> {
            var createMovieRequest = CreateMovieRequest.builder()
                    .id(UUID.randomUUID().toString())
                    .title(row.get("title"))
                    .description(row.get("description"))
                    .productionYear(Integer.parseInt(row.get("productionYear")))
                    .actors(Arrays.stream(row.get("actors").split(",")).map(String::trim).collect(toSet()))
                    .generes(Arrays.stream(row.get("generes").split(",")).map(String::trim).map(Genere::valueOf).collect(toSet()))
                    .duration(Duration.parse(row.get("duration")))
                    .build();
            cymesClient.createMovie(createMovieRequest);
        });
    }

    @Then("admin movies list contains following movies:")
    public void admin_movies_list_contains_following_movies(List<Map<String, String>> dataTable) {
        PaginatedSearchResultsResponse<BasicMovieInformationResponse> movies = cymesClient.getMovies();
        assertThat(movies.getResults().size()).isEqualTo(dataTable.size());
        dataTable.forEach((movieAttributes) -> {
            BasicMovieInformationResponse movie = movies.getResults().get(dataTable.indexOf(movieAttributes));
            assertThat(movie.getId()).isNotNull();
            assertThat(movie.getTitle()).isEqualTo(movieAttributes.get("title"));
        });
    }
}
