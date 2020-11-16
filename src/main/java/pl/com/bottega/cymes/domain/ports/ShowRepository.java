package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.Show;

public interface ShowRepository {
    void save(Show show);

    boolean containsShowsCollidingWith(Show show);
}
