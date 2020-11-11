package pl.com.bottega.cymes.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.com.bottega.cymes.domain.model.commands.CreateMovieCommand;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Movie {

    private final UUID id;
    private String title;
    private Integer productionYear;
    private String description;
    private Duration duration;
    private Set<String> actors;
    private Set<Genere> generes;

    public Movie(CreateMovieCommand command) {
        this.id = command.getId();
        this.title = command.getTitle();
        this.productionYear = command.getProductionYear();
        this.description = command.getDescription();
        this.duration = command.getDuration();
        this.actors = command.getActors();
        this.generes = command.getGeneres();
    }
}
