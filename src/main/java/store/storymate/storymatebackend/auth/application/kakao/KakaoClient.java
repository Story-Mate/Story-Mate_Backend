package store.storymate.storymatebackend.auth.application.kakao;

import static store.storymate.storymatebackend.global.constant.SecurityConstants.KAKAO_USER_ME_URL;
import static store.storymate.storymatebackend.global.constant.SecurityConstants.TOKEN_PREFIX;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import store.storymate.storymatebackend.auth.dto.response.KakaoAuthResponse;
import store.storymate.storymatebackend.auth.dto.response.SocialClientResponse;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;


@Component
@RequiredArgsConstructor
public class KakaoClient {
    private final RestClient restClient;


    public SocialClientResponse authenticateFromKakao(String token) {
        KakaoAuthResponse kakaoAuthResponse =
                restClient
                        .get()
                        .uri(KAKAO_USER_ME_URL)
                        .header("Authorization", TOKEN_PREFIX + token)
                        .exchange(
                                (request, response) -> {
                                    if (!response.getStatusCode().is2xxSuccessful()) {
                                        throw new BadRequestException("카카오 통신에 실패하였습니다.");
                                    }
                                    return Objects.requireNonNull(
                                            response.bodyTo(KakaoAuthResponse.class));
                                });

        return new SocialClientResponse(
                kakaoAuthResponse.kakaoAccount().email(), kakaoAuthResponse.id().toString(),
                kakaoAuthResponse.properties().nickname(),
                kakaoAuthResponse.properties().profile_image());
    }
}
