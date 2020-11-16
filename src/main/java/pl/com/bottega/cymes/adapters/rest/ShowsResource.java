package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.commands.ScheduleShowCommand;
import pl.com.bottega.cymes.domain.model.queries.ShowsQuery;
import pl.com.bottega.cymes.domain.ports.RepertoireBrowser;
import pl.com.bottega.cymes.domain.ports.ShowScheduler;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CONFLICT;
import static pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import static pl.com.bottega.cymes.domain.ports.RepertoireBrowser.SearchedShow;
import static pl.com.bottega.cymes.domain.ports.ShowScheduler.CinemaHallOccupiedException;

@RestController
@RequestMapping("/shows")
public class ShowsResource {

    @Autowired
    private ShowScheduler showScheduler;

    @Autowired
    private RepertoireBrowser repertoireBrowser;

    @PostMapping
    public void schedule(@RequestBody ScheduleShowRequest scheduleShowRequest) {
        showScheduler.schedule(scheduleShowRequest.toCommand());
    }

    @GetMapping
    public List<SearchedShowResponse> search(SearchShowsRequest searchShowsRequest) {
        return repertoireBrowser.search(searchShowsRequest.toQuery()).stream().map((show) -> new SearchedShowResponse(show)).collect(toList());
    }

    @ExceptionHandler(CinemaHallOccupiedException.class)
    public ResponseEntity<Errors> handleCinemaHallOccupiedException(CinemaHallOccupiedException ex) {
        return new ResponseEntity<>(Errors.of(ex), CONFLICT);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleShowRequest {
        private String cinemaId;
        private String cinemaHallNumber;
        private String movieId;
        private Instant startTime;

        public ScheduleShowCommand toCommand() {
            return new ScheduleShowCommand(
                    new CinemaHallId(UUID.fromString(cinemaId), cinemaHallNumber),
                    UUID.fromString(movieId),
                    startTime
            );
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchShowsRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate day;
        private String city;
        private String cinemaId;
        private String movieId;

        public ShowsQuery toQuery() {
            return new ShowsQuery(day,
                    city == null || city.trim().length() == 0 ? null : city.trim(),
                    cinemaId == null ? null : UUID.fromString(cinemaId),
                    movieId == null ? null : UUID.fromString(movieId)
            );
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchedShowResponse {
        private String showId;
        private String title;
        private String cinemaName;
        private String city;
        private String cinemaId;
        private Instant startTime;

        public SearchedShowResponse(SearchedShow searchedShow) {
            this(
                    searchedShow.getShowId().toString(),
                    searchedShow.getTitle(),
                    searchedShow.getCinemaName(),
                    searchedShow.getCity(),
                    searchedShow.getCinemaId().toString(),
                    searchedShow.getStartTime()
            );
        }
    }
}
