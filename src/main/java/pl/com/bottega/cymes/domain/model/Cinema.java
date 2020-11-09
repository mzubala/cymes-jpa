package pl.com.bottega.cymes.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class Cinema {
    @NonNull
    UUID id;
    @NonNull
    String city;
    @NonNull
    String name;
}
