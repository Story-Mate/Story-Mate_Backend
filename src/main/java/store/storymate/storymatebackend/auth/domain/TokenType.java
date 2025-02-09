package store.storymate.storymatebackend.auth.domain;

import java.security.InvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
    ACCESS("access"),
    REFRESH("refresh"),
    ;
    private final String value;

    public static TokenType from(String typeKey) {
        return switch (typeKey.toUpperCase()) {
            case "ACCESS" -> ACCESS;
            case "REFRESH" -> REFRESH;
            default -> throw new InvalidParameterException();
        };
    }
}
