package store.storymate.storymatebackend.auth.api.dto.response;

import lombok.Builder;

@Builder
public record AccessAndRefreshTokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
