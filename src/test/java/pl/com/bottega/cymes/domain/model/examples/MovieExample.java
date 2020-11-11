package pl.com.bottega.cymes.domain.model.examples;

import lombok.Builder;
import pl.com.bottega.cymes.domain.model.Genere;
import pl.com.bottega.cymes.domain.model.Movie;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Builder
public class MovieExample {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Builder.Default
    private String title = "Batman";
    @Builder.Default
    private Integer productionYear = 1989;
    @Builder.Default
    private String description = "A good film";
    @Builder.Default
    private Duration duration = Duration.parse("PT2H");
    @Builder.Default
    private Set<String> actors = Set.of("Michael Keaton", "Jack Nicholson");
    @Builder.Default
    private Set<Genere> generes = Set.of(Genere.ACTION, Genere.DRAMA);

    public Movie toDomain() {
        return new Movie(id, title, productionYear, description, duration, actors, generes);
    }
}
