package pl.com.bottega.cymes.domain.model.commands;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CreateCinemaCommand {
    @NonNull
    UUID id;
    @NonNull
    String city;
    @NonNull
    String name;
}
