package store.storymate.storymatebackend.auth.domain;

import java.security.InvalidParameterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    KAKAO("KAKAO"),
    ;
    private final String value;

    public static OAuthProvider from(String provider) {
        return switch (provider.toUpperCase()) {
            case "KAKAO" -> KAKAO;
            default -> throw new InvalidParameterException();
        };
    }
}
