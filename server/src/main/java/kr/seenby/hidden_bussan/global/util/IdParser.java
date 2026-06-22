package kr.seenby.hidden_bussan.global.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class IdParser {

    private IdParser() {
    }

    public static Long parse(String id, String fieldName) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be a numeric id");
        }
    }
}
