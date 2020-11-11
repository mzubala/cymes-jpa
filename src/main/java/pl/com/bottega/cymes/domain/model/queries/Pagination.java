package pl.com.bottega.cymes.domain.model.queries;

import lombok.Value;
import lombok.With;

@Value
@With
public class Pagination {
    Integer perPage;
    Integer pageNumber;
}
