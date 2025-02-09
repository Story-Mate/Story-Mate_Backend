package store.storymate.storymatebackend.auth.dto.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken) {
    public static AuthTokenResponse of(
            TokenPairResponse tokenPairResponse) {
        return new AuthTokenResponse(
                tokenPairResponse.accessToken(),
                tokenPairResponse.refreshToken()
        );
    }
}
