package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

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
