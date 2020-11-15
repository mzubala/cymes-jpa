package pl.com.bottega.cymes.domain.ports;

import lombok.Value;
import pl.com.bottega.cymes.domain.model.queries.ShowsQuery;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RepertoireBrowser {
    List<SearchedShow> search(ShowsQuery query);

    @Value
    class SearchedShow {
        UUID showId;
        String title;
        String cinemaName;
        String city;
        UUID cinemaId;
        Instant startTime;
    }
}
