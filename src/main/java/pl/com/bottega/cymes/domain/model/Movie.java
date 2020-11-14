package pl.com.bottega.cymes.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Movie {
    @NonNull
    private final UUID id;
    @NonNull
    private String title;
    @NonNull
    private Integer productionYear;
    @NonNull
    private String description;
    @NonNull
    private Duration duration;
    @NonNull
    private Set<String> actors;
    @NonNull
    private Set<Genere> generes;
}
