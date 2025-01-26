package store.storymate.storymatebackend.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.auth.api.dto.request.RefreshTokenRequestDto;
import store.storymate.storymatebackend.auth.api.dto.request.TokenRequestDto;
import store.storymate.storymatebackend.auth.api.dto.response.IdTokenResponseDto;
import store.storymate.storymatebackend.auth.api.dto.response.MemberInfoResponseDto;
import store.storymate.storymatebackend.auth.api.dto.response.MemberLoginResponseDto;
import store.storymate.storymatebackend.auth.application.AuthMemberService;
import store.storymate.storymatebackend.auth.application.AuthService;
import store.storymate.storymatebackend.auth.application.AuthServiceFactory;
import store.storymate.storymatebackend.auth.application.TokenService;
import store.storymate.storymatebackend.global.jwt.api.dto.TokenDto;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.member.domain.SocialType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController implements AuthDocs {
    private final AuthServiceFactory authServiceFactory;
    private final AuthMemberService authMemberService;
    private final TokenService tokenService;

    @Override
    @GetMapping("oauth2/callback/{provider}")
    public IdTokenResponseDto callback(@PathVariable String provider, @RequestParam String code) {
        return authServiceFactory.getAuthService(provider).getIdToken(code);
    }

    @Override
    @PostMapping("/{provider}/token")
    public ApiResponseTemplate<TokenDto> generateAccessAndRefreshToken(@PathVariable String provider,
                                                                       @RequestBody TokenRequestDto tokenRequestDTO) {
        AuthService authService = authServiceFactory.getAuthService(provider);
        MemberInfoResponseDto memberInfoResponseDTO = authService.getUserInfo(tokenRequestDTO.authCode());
        System.out.println(memberInfoResponseDTO.email() +  "," + memberInfoResponseDTO.picture() + "," + memberInfoResponseDTO.nickname() + "," + memberInfoResponseDTO.birthYear());
        MemberLoginResponseDto memberLoginResponseDto = authMemberService.saveMemberInfo(memberInfoResponseDTO,
                SocialType.valueOf(provider.toUpperCase()));
        TokenDto tokenDto = tokenService.getToken(memberLoginResponseDto);

        return ApiResponseTemplate.ok("토큰 발급", tokenDto);
    }

    @Override
    @PostMapping("/token/access")
    public ApiResponseTemplate<TokenDto> generateAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO) {
        TokenDto getToken = tokenService.generateAccessToken(refreshTokenRequestDTO);

        return ApiResponseTemplate.ok("액세스 토큰 발급", getToken);
    }
}
