package store.storymate.storymatebackend.auth.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.auth.application.AuthService;
import store.storymate.storymatebackend.auth.domain.OAuthProvider;
import store.storymate.storymatebackend.auth.dto.request.RefreshTokenRequest;
import store.storymate.storymatebackend.auth.dto.request.SocialLoginRequest;
import store.storymate.storymatebackend.auth.dto.response.AuthTokenResponse;
import store.storymate.storymatebackend.auth.dto.response.SocialClientResponse;

@Tag(name = "[인증 API]", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 후 access 토큰과 refresh 토큰을 발급합니다.")
    @PostMapping("/social-login/{provider}")
    public AuthTokenResponse socialLogin(
            @PathVariable(name = "provider")
            @Parameter(example = "kakao", description = "OAuth 제공자")
            String provider,
            @RequestBody @Valid SocialLoginRequest request) {
        OAuthProvider oAuthProvider = OAuthProvider.from(provider);

        SocialClientResponse socialClientResponse =
                authService.authenticateFromProvider(oAuthProvider, request.token());

        return authService.socialLogin(
                oAuthProvider, socialClientResponse.oauthId(), socialClientResponse.email(),
                socialClientResponse.nickname(), socialClientResponse.profileImage());
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용해 새로운 access 토큰을 발급합니다.")
    @PostMapping("/reissue")
    public AuthTokenResponse reissueTokenPair(@RequestBody @Valid RefreshTokenRequest request) {
        return authService.reissueTokenPair(request);
    }

    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
