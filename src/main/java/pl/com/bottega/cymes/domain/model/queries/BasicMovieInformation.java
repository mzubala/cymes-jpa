package pl.com.bottega.cymes.domain.model.queries;

import lombok.Value;
import java.util.UUID;

@Value
public class BasicMovieInformation {
    UUID id;
    String title;
}
