package pl.com.bottega.cymes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

import static pl.com.bottega.cymes.domain.model.CinemaHall.*;

@AllArgsConstructor
@Getter
public class Show {
    private final UUID id;
    private final CinemaHallId cinemaHallId;
    private final UUID movieId;
    private final Instant startAt;
    private final Instant endAt;
}
