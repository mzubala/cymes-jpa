package pl.com.bottega.cymes.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Do;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaRequest;
import pl.com.bottega.cymes.adapters.rest.Errors;
import pl.com.bottega.cymes.adapters.rest.PaginatedSearchResultsResponse;
import pl.com.bottega.cymes.adapters.rest.ReservationResource;
import pl.com.bottega.cymes.client.CymesClient;
import pl.com.bottega.cymes.client.DbClient;
import pl.com.bottega.cymes.domain.model.Genere;
import pl.com.bottega.cymes.domain.model.pricing.TicketKind;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.com.bottega.cymes.adapters.rest.CinemasResource.CreateCinemaHallRequest;
import static pl.com.bottega.cymes.adapters.rest.MoviesResource.BasicMovieInformationResponse;
import static pl.com.bottega.cymes.adapters.rest.MoviesResource.CreateMovieRequest;
import static pl.com.bottega.cymes.adapters.rest.PriceListsResource.SaveMoviePriceListRequest;
import static pl.com.bottega.cymes.adapters.rest.ReservationResource.SelectTicketsRequest;
import static pl.com.bottega.cymes.adapters.rest.ShowsResource.ScheduleShowRequest;
import static pl.com.bottega.cymes.adapters.rest.ShowsResource.SearchShowsRequest;

public class StepDefs extends SpringAcceptanceTest {

    private CymesClient cymesClient;

    @LocalServerPort
    private int port;

    @Autowired
    private DbClient dbClient;

    private ReservationFeatureObject reservationFeatureObject;

    @Before
    public void setup() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofMinutes(10L)).build();
        cymesClient = new CymesClient(webTestClient);
        dbClient.clean();
    }

    @Given("cinema {string} in {string} has been created")
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

    @When("network admin creates cinema hall {string} in {string} {string}:")
    @Given("network admin created cinema hall {string} in {string} {string}:")
    public void network_admin_creates_cinema_hall_in(String cinemaHallNumber, String city, String cinemaName, io.cucumber.datatable.DataTable dataTable) {
        var cinemaId = cymesClient.getCinemaId(cinemaName, city);
        var request = new CreateCinemaHallRequest(cinemaHallNumber, dataTable.asList());
        cymesClient.createCinemaHall(cinemaId, request);
    }

    @Then("cinema halls list in {string} {string} contains:")
    public void cinema_halls_list_in_contains(String city, String cinemaName, io.cucumber.datatable.DataTable dataTable) {
        var cinemaId = cymesClient.getCinemaId(cinemaName, city);
        var cinemaHalls = cymesClient.getCinemaHalls(cinemaId);
        assertThat(cinemaHalls).isEqualTo(dataTable.asList());
    }

    @Then("cinema hall {string} in {string} {string} contains following rows:")
    public void cinema_hall_in_contains_following_rows(String cinemaHallNumber, String city, String cinemaName, io.cucumber.datatable.DataTable dataTable) {
        var cinemaId = cymesClient.getCinemaId(cinemaName, city);
        var cinemaHallInfo = cymesClient.getCinemaHall(cinemaId, cinemaHallNumber);
        assertThat(cinemaHallInfo.getRows()).isEqualTo(dataTable.asList());
    }

    @Given("network admin scheduled shows:")
    @When("network admin schedules shows:")
    public void network_admin_schedules_shows(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach((showAttributes) -> {
            var cinemaId = cymesClient.getCinemaId(showAttributes.get("cinema"), showAttributes.get("city"));
            var movieId = cymesClient.getMovieId(showAttributes.get("movie"));
            cymesClient.scheduleShow(ScheduleShowRequest.builder()
                    .cinemaId(cinemaId)
                    .cinemaHallNumber(showAttributes.get("hall"))
                    .movieId(movieId)
                    .startTime(Instant.parse(showAttributes.get("startTime")))
                    .build());
        });
    }

    @Then("customer can see repertoire for {string} {string} on {string}:")
    public void customer_can_see_repertoire_for_on(String city, String cinemaName, String dateString, io.cucumber.datatable.DataTable dataTable) {
        var cinemaId = cymesClient.getCinemaId(cinemaName, city);
        var shows = cymesClient.searchShows(SearchShowsRequest.builder().cinemaId(cinemaId).day(LocalDate.parse(dateString)).build());
        assertThat(
                shows.stream().map((show) -> List.of(show.getTitle(), DateTimeFormatter.ISO_INSTANT.format(show.getStartTime()))).collect(toList())
        ).isEqualTo(dataTable.asLists());
    }

    @Then("scheduling fails with conflict for shows:")
    public void scheduling_fails_with_conflict_for_shows(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach((showAttributes) -> {
            var cinemaId = cymesClient.getCinemaId(showAttributes.get("cinema"), showAttributes.get("city"));
            var movieId = cymesClient.getMovieId(showAttributes.get("movie"));
            cymesClient.trySchedulingShow(ScheduleShowRequest.builder()
                    .cinemaId(cinemaId)
                    .cinemaHallNumber(showAttributes.get("hall"))
                    .movieId(movieId)
                    .startTime(Instant.parse(showAttributes.get("startTime")))
                    .build()
            ).expectStatus().isEqualTo(HttpStatus.CONFLICT);
        });
    }

    @When("network admin defines show prices for movie {string}:")
    public void network_admin_defines_show_prices_for_movie(String movieTitle, io.cucumber.datatable.DataTable dataTable) {
        String movieId = cymesClient.getMovieId(movieTitle);
        cymesClient.saveMoviePriceList(new SaveMoviePriceListRequest(
                movieId,
                toPriceList(dataTable)
        ));
    }

    private HashMap<TicketKind, BigDecimal> toPriceList(io.cucumber.datatable.DataTable dataTable) {
        return dataTable.asLists().stream().reduce(
                new HashMap<>(),
                (acc, element) -> {
                    acc.put(TicketKind.valueOf(element.get(0)), BigDecimal.valueOf(Double.parseDouble(element.get(1))));
                    return acc;
                },
                (x, y) -> {
                    x.putAll(y);
                    return x;
                }
        );
    }

    @When("customer wants to buy tickets for {string} in {string} {string} at {string}")
    public void customer_wants_to_buy_tickets_for_in_at(String movieTitle, String city, String cinemaName, String showStartAt) {
        reservationFeatureObject = new ReservationFeatureObject(cymesClient);
        reservationFeatureObject.selectShow(movieTitle, city, cinemaName, showStartAt);
    }

    @Then("customer sees following ticket prices:")
    public void customer_sees_following_ticket_prices(io.cucumber.datatable.DataTable dataTable) {
        assertThat(reservationFeatureObject.getPriceList().getPriceList()).isEqualTo(toPriceList(dataTable));
    }

    @When("customer selects to buy following tickets:")
    public void customer_selects_to_buy_following_tickets(io.cucumber.datatable.DataTable dataTable) {
        reservationFeatureObject.selectTickets(new SelectTicketsRequest(dataTable.asLists().stream().reduce(
                        new HashMap<>(),
                        (acc, element) -> {
                            acc.put(TicketKind.valueOf(element.get(0)), Integer.parseInt(element.get(1)));
                            return acc;
                        }, (m1, m2) -> {
                            m1.putAll(m2);
                            return m1;
                        }
                )))
        ;
    }

    @Then("system calculates tickets price as {string}:")
    public void system_calculates_tickets_price_as(String price, io.cucumber.datatable.DataTable dataTable) {
        var reservationDetails = reservationFeatureObject.getReservation();
        assertThat(reservationDetails.getReceipt().getTotal()).isEqualTo(BigDecimal.valueOf(Double.parseDouble(price)));
        assertThat(reservationDetails.getReceipt().getItems()).containsAll(dataTable.asMaps().stream().map((row) -> new ReservationResource.TicketsReceiptItemResponse(
                TicketKind.valueOf(row.get("ticketKind")),
                Integer.parseInt(row.get("count")),
                BigDecimal.valueOf(Double.parseDouble(row.get("price"))),
                BigDecimal.valueOf(Double.parseDouble(row.get("total")))
        )).collect(toList()));
    }
}
