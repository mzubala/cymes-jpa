package pl.com.bottega.cymes.domain.model;

import org.junit.jupiter.api.Test;
import pl.com.bottega.cymes.domain.model.CinemaHall.CinemaHallId;
import pl.com.bottega.cymes.domain.model.CinemaHall.InvalidLayoutException;
import pl.com.bottega.cymes.domain.model.CinemaHall.Row;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CinemaHallTest {
    @Test
    public void createsNewHallWithIdAndLayout() {
        CinemaHallId id = new CinemaHallId(UUID.randomUUID(), "1");
        var hall = CinemaHall.of(id, List.of(
           "_XXX_XX",
           "XXXXXXX"
        ));

        assertThat(hall.getId()).isEqualTo(id);
        assertThat(hall.getRows()).isEqualTo(List.of(
                new Row("_XXX_XX"),
                new Row("XXXXXXX"))
        );
    }

    @Test
    public void rowsMustHaveSameNumberOfElements() {
        CinemaHallId id = new CinemaHallId(UUID.randomUUID(), "1");
        assertThatThrownBy(() -> CinemaHall.of(id, List.of(
                "XXX",
                "XX"
        ))).isInstanceOf(InvalidLayoutException.class);
    }

    @Test
    public void rowsMustContainSeatsOrCorridorElements() {
        CinemaHallId id = new CinemaHallId(UUID.randomUUID(), "1");
        assertThatThrownBy(() -> CinemaHall.of(id, List.of(
                "xXX_"
        ))).isInstanceOf(InvalidLayoutException.class);
        assertThatThrownBy(() -> CinemaHall.of(id, List.of(
                "?XX_"
        ))).isInstanceOf(InvalidLayoutException.class);
    }
}
