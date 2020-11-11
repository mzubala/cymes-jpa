package pl.com.bottega.cymes.domain.model.queries;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class BasicMovieQuery {
    String phrase;
    @NonNull
    Pagination pagination;
}
