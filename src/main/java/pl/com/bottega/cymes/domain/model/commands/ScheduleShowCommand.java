package pl.com.bottega.cymes.domain.model.commands;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

import static pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;

@Value
public class ScheduleShowCommand {
    CinemaHallId cinemaHallId;
    UUID movieId;
    Instant startTime;
}
