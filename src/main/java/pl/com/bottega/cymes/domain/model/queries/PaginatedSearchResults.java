package pl.com.bottega.cymes.domain.model.queries;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PaginatedSearchResults<T> {
    @NonNull
    List<T> results;
    @NonNull
    Pagination pagination;
    @NonNull
    Long totalCount;
    @NonNull
    Long pagesCount;
}
