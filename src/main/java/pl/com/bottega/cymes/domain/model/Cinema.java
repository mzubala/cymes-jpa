package pl.com.bottega.cymes.domain.model;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Value
public class Cinema {
    UUID id;
    String city;
    String name;

    public Cinema(@NonNull UUID id, @NonNull String city, @NonNull String name) {
        this.id = id;
        this.city = withoutSpaces(city);
        this.name = withoutSpaces(name);
    }

    private String withoutSpaces(String input) {
        return StringUtils.join(input.trim().split("\\s+"), " ");
    }
}
