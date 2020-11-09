package pl.com.bottega.cymes.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaRequest;
import pl.com.bottega.cymes.adapters.rest.Errors;
import pl.com.bottega.cymes.client.CymesClient;
import pl.com.bottega.cymes.client.DbClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Then("cinemas list contains following cinemas:")
    public void cinemas_list_contains_following_cinemas(List<Map<String, String>> cinemasTable) {
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


}
