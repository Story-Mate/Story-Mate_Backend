package store.storymate.storymatebackend.auth.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemberInfoResponseDto(
        String email,
        String picture,
        String nickname,
        @JsonProperty("birthyear") String birthYear
) {
}
