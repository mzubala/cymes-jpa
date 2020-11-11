package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.bottega.cymes.domain.model.queries.Pagination;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private Integer perPage;
    private Integer pageSize;

    public PaginationResponse(Pagination pagination) {
        this.perPage = pagination.getPerPage();
        this.pageSize = pagination.getPageNumber();
    }
}
