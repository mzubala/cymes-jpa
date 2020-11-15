package pl.com.bottega.cymes.domain.model.queries;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class ShowsQuery {
    @NonNull
     LocalDate day;
     String city;
     UUID cinemaId;
     UUID movieId;
}
