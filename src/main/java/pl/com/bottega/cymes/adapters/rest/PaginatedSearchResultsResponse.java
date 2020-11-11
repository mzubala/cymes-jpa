package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedSearchResultsResponse<T> {
    @NonNull
    List<T> results;
    @NonNull
    PaginationResponse pagination;
    @NonNull
    Long totalCount;
    @NonNull
    Long pagesCount;
}
