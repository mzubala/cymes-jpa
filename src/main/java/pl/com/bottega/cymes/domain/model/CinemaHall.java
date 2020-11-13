package pl.com.bottega.cymes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@AllArgsConstructor
public class CinemaHall {

    private final CinemaHallId id;
    private List<Row> rows;

    public static CinemaHall of(CinemaHallId id, List<String> layoutRows) {
        validateLayout(layoutRows);
        return new CinemaHall(id, layoutRows.stream().map(Row::new).collect(toUnmodifiableList()));
    }

    private static void validateLayout(List<String> layoutRows) {
        try {
            checkArgument(layoutRows.size() > 0);
            layoutRows.forEach(row -> checkArgument(row != null && row.length() == layoutRows.get(0).length()));
        } catch (IllegalArgumentException ex) {
            throw new InvalidLayoutException();
        }
    }

    @Value
    public static class CinemaHallId {
        UUID cinemaId;
        String number;
    }

    @Value
    public static class Row {
        String layout;

        public Row(String layout) {
            validateLayout(layout);
            this.layout = layout;
        }

        private void validateLayout(String layout) {
            layout.chars().forEach(RowElement::of);
        }
    }

    public enum RowElement {
        SEAT('X'), CORRIDOR('_');

        int sign;

        RowElement(char sign) {
            this.sign = sign;
        }

        private static RowElement of(int sign) {
            return stream(values())
                    .filter(rowElement -> rowElement.sign == sign)
                    .findFirst()
                    .orElseThrow(InvalidLayoutException::new);
        }
    }

    public static class InvalidLayoutException extends RuntimeException {}
}
