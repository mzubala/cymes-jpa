package pl.com.bottega.cymes.domain.model.commands;

import lombok.Getter;
import pl.com.bottega.cymes.domain.model.Genere;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Getter
public class CreateMovieCommand {
    UUID id;
    String title;
    Integer productionYear;
    String description;
    Duration duration;
    Set<String> actors;
    Set<Genere> generes;

    public CreateMovieCommand(UUID id, String title, Integer productionYear, String description, Duration duration, Set<String> actors, Set<Genere> generes) {
        this.id = id;
        this.title = title;
        this.productionYear = productionYear;
        this.description = description;
        this.duration = duration;
        this.actors = actors;
        this.generes = generes;
    }
}
