package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.Show;

import java.util.UUID;

public interface ShowRepository {
    void save(Show show);

    boolean containsShowsCollidingWith(Show show);

    Show get(UUID showId);
}
