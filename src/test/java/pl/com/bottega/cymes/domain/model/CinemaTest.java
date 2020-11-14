package pl.com.bottega.cymes.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CinemaTest {
    @Test
    public void cannotCreateCinemaWithoutRequiredInformation() {
        assertThatThrownBy(() -> new Cinema(null, "Lublin", "Plaza")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Cinema(UUID.randomUUID(), null, "Plaza")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Cinema(UUID.randomUUID(), "Lublin", null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void stripsWhiteCharactersFromCityAndName() {
        var cinema = new Cinema(UUID.randomUUID(), "  Zielona    Góra   ", "   Złote   Tarasy  ");

        assertThat(cinema.getCity()).isEqualTo("Zielona Góra");
        assertThat(cinema.getName()).isEqualTo("Złote Tarasy");
    }
}
