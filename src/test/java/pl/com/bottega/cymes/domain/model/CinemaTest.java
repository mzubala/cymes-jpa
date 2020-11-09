package pl.com.bottega.cymes.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CinemaTest {
    @Test
    public void cannotCreateCinemaWithoutRequiredInformation() {
        assertThatThrownBy(() -> new Cinema(null, "Lublin", "Plaza")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Cinema(UUID.randomUUID(), null, "Plaza")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Cinema(UUID.randomUUID(), "Lublin", null)).isInstanceOf(NullPointerException.class);
    }
}
