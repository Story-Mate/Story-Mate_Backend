package store.storymate.storymatebackend.auth.api.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

public record IdTokenResponseDto(
        JsonNode idToken
) {
}
