package pl.com.bottega.cymes.adapters.rest;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.cymes.domain.model.Genere;
import pl.com.bottega.cymes.domain.model.commands.CreateMovieCommand;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieInformation;
import pl.com.bottega.cymes.domain.model.queries.BasicMovieQuery;
import pl.com.bottega.cymes.domain.model.queries.Pagination;
import pl.com.bottega.cymes.domain.ports.AdminService;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/movies")
public class MoviesResource {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public void create(@RequestBody CreateMovieRequest createMovieRequest) {
        adminService.createMovie(new CreateMovieCommand(
                UUID.fromString(createMovieRequest.id),
                createMovieRequest.title,
                createMovieRequest.productionYear,
                createMovieRequest.description,
                createMovieRequest.duration,
                createMovieRequest.actors,
                createMovieRequest.generes
        ));
    }

    @GetMapping
    public PaginatedSearchResultsResponse<BasicMovieInformationResponse> search(SearchMoviesRequest request) {
        var paginatedSearchResults = adminService.search(request.toQuery());
        return new PaginatedSearchResultsResponse<>(
                paginatedSearchResults.getResults().stream().map(BasicMovieInformationResponse::new).collect(toList()),
                new PaginationResponse(paginatedSearchResults.getPagination()),
                paginatedSearchResults.getTotalCount(),
                paginatedSearchResults.getPagesCount()
        );
    }

    @Data
    @Builder
    public static class CreateMovieRequest {
        private String id;
        private String title;
        private Integer productionYear;
        private String description;
        private Duration duration;
        private Set<String> actors;
        private Set<Genere> generes;
    }

    @Data
    @NoArgsConstructor
    public static class BasicMovieInformationResponse {
        private String id;
        private String title;

        BasicMovieInformationResponse(BasicMovieInformation movie) {
            this.id = movie.getId().toString();
            this.title = movie.getTitle();
        }
    }

    @Data
    public static class SearchMoviesRequest {
        private String phrase;
        private Integer perPage = 50;
        private Integer pageNumber = 1;

        public BasicMovieQuery toQuery() {
            return new BasicMovieQuery(phrase, new Pagination(perPage, pageNumber));
        }
    }
}