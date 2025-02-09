package store.storymate.storymatebackend.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAuthResponse(
        Long id,
        @JsonProperty("kakao_account") KakaoAccountResponse kakaoAccount,
        @JsonProperty("properties") PropertiesResponse properties) {
    public static record KakaoAccountResponse(
            String email
    ) {
    }

    public static record PropertiesResponse(
            String nickname, String profile_image
    ) {
    }
}
