package store.storymate.storymatebackend.auth.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.auth.application.kakao.KakaoClient;
import store.storymate.storymatebackend.auth.domain.OAuthProvider;
import store.storymate.storymatebackend.auth.dto.RefreshTokenDto;
import store.storymate.storymatebackend.auth.dto.request.RefreshTokenRequest;
import store.storymate.storymatebackend.auth.dto.response.AuthTokenResponse;
import store.storymate.storymatebackend.auth.dto.response.SocialClientResponse;
import store.storymate.storymatebackend.auth.dto.response.TokenPairResponse;
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.jwt.JwtTokenProvider;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.MemberRole;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;

    private final KakaoClient kakaoClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberUtil memberUtil;

    public SocialClientResponse authenticateFromProvider(OAuthProvider provider, String token) {
        return switch (provider) {
            case KAKAO -> kakaoClient.authenticateFromKakao(token);
        };
    }

    public AuthTokenResponse socialLogin(
            OAuthProvider oAuthProvider, String oauthId, String email, String nickname,
            String profileImage) {
        Optional<Member> memberOptional =
                memberRepository.findByOauthInfoOauthProviderAndOauthInfoOauthId(
                        oAuthProvider.getValue(), oauthId);

        return memberOptional
                .map(
                        member -> {
                            TokenPairResponse tokenPair = getLoginResponse(member);
                            member.updateLastLoginAt();
                            updateMemberNormalStatus(member);
                            log.info("소셜 로그인 진행: {}", member.getId());
                            return AuthTokenResponse.of(tokenPair);
                        })
                .orElseGet(
                        () -> {
                            Member newMember =
                                    Member.createMember(oAuthProvider, oauthId, email, nickname,
                                            profileImage);
                            memberRepository.save(newMember);

                            TokenPairResponse tokenPair = jwtTokenProvider.generateTokenPair(newMember.getId(),
                                    MemberRole.USER);
                            newMember.updateLastLoginAt();
                            log.info("회원가입 진행: {}", newMember.getId());
                            return AuthTokenResponse.of(tokenPair);
                        });
    }

    @Transactional(readOnly = true)
    public AuthTokenResponse reissueTokenPair(RefreshTokenRequest request) {
        // 리프레시 토큰을 이용해 새로운 액세스 토큰 발급
        RefreshTokenDto refreshTokenDto =
                jwtTokenProvider.retrieveRefreshToken(request.refreshToken());
        RefreshTokenDto refreshToken =
                jwtTokenProvider.createRefreshTokenDto(refreshTokenDto.memberId());

        Member member = memberUtil.getMemberByMemberId(refreshToken.memberId());

        TokenPairResponse tokenPair = getLoginResponse(member);
        return AuthTokenResponse.of(tokenPair);
    }

    private TokenPairResponse getLoginResponse(Member member) {
        return jwtTokenProvider.generateTokenPair(member.getId(), MemberRole.USER);
    }

    private void updateMemberNormalStatus(Member member) {
        if (member.getStatus() == Status.DELETED) {
            member.updateStatus(Status.ACTIVE);
        }
    }

    public void logout() {
        Member member = memberUtil.getCurrentMember();
        member.updateStatus(Status.INACTIVE);
    }
}

