package pl.com.bottega.cymes.adapters.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Errors {

    List<Error> errors;

    public static Errors of(Error... errors) {
        return new Errors(List.of(errors));
    }

    public static Errors of(Exception exception) {
        return new Errors(List.of(Error.of(exception)));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        @NonNull
        String code;
        String message;

        public Error(String code) {
            this(code, null);
        }

        public static Error of(Exception exception) {
            return new Error(exception.getClass().getSimpleName().replace("Exception", "Error"), exception.getMessage());
        }
    }
}
