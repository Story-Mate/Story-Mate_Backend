package store.storymate.storymatebackend.global.jwt;

import static store.storymate.storymatebackend.global.constant.SecurityConstants.TOKEN_ROLE_NAME;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.auth.domain.RefreshToken;
import store.storymate.storymatebackend.auth.dto.AccessTokenDto;
import store.storymate.storymatebackend.auth.dto.RefreshTokenDto;
import store.storymate.storymatebackend.auth.dto.response.TokenPairResponse;
import store.storymate.storymatebackend.auth.repository.RefreshTokenRepository;
import store.storymate.storymatebackend.member.domain.MemberRole;


@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenPairResponse generateTokenPair(Long memberId, MemberRole memberRole) {
        String accessToken = createAccessToken(memberId, memberRole);
        String refreshToken = createRefreshToken(memberId);
        return TokenPairResponse.of(accessToken, refreshToken);
    }

    private String createAccessToken(Long memberId, MemberRole memberRole) {
        return jwtUtil.generateAccessToken(memberId, memberRole);
    }

    public AccessTokenDto createAccessTokenDto(Long memberId, MemberRole memberRole) {
        return jwtUtil.generateAccessTokenDto(memberId, memberRole);
    }

    private String createRefreshToken(Long memberId) {
        String token = jwtUtil.generateRefreshToken(memberId);
        saveRefreshTokenToRedis(memberId, token, jwtUtil.getRefreshTokenExpirationTime());
        return token;
    }

    public RefreshTokenDto createRefreshTokenDto(Long memberId) {
        RefreshTokenDto refreshTokenDto = jwtUtil.generateRefreshTokenDto(memberId);
        saveRefreshTokenToRedis(memberId, refreshTokenDto.tokenValue(), refreshTokenDto.ttl());
        return refreshTokenDto;
    }

    private void saveRefreshTokenToRedis(Long memberId, String refreshTokenDto, Long ttl) {
        RefreshToken refreshToken =
                RefreshToken.builder().memberId(memberId).token(refreshTokenDto).ttl(ttl).build();
        refreshTokenRepository.save(refreshToken);
    }

    public AccessTokenDto retrieveAccessToken(String accessTokenValue) {
        try {
            return jwtUtil.parseAccessToken(accessTokenValue);
        } catch (Exception e) {
            return null;
        }
    }

    public RefreshTokenDto retrieveRefreshToken(String refreshTokenValue) {
        RefreshTokenDto refreshTokenDto = parseRefreshToken(refreshTokenValue);

        if (refreshTokenDto == null) {
            return null;
        }

        Optional<RefreshToken> refreshToken = getRefreshTokenFromRedis(refreshTokenDto.memberId());

        if (refreshToken.isPresent()) {
            return refreshTokenDto;
        }

        return null;
    }

    private Optional<RefreshToken> getRefreshTokenFromRedis(Long memberId) {
        return refreshTokenRepository.findById(memberId);
    }

    private RefreshTokenDto parseRefreshToken(String refreshTokenValue) {
        try {
            return jwtUtil.parseRefreshToken(refreshTokenValue);
        } catch (Exception e) {
            return null;
        }
    }

    public AccessTokenDto reissueAccessTokenIfExpired(String accessTokenValue) {
        // AT가 만료된 경우 AT 재발급, 만료되지 않은 경우 null 반환
        try {
            jwtUtil.parseAccessToken(accessTokenValue);
            return null;
        } catch (ExpiredJwtException e) {
            Long memberId = Long.parseLong(e.getClaims().getSubject());
            MemberRole memberRole =
                    MemberRole.valueOf(e.getClaims().get(TOKEN_ROLE_NAME, String.class));
            return createAccessTokenDto(memberId, memberRole);
        }
    }

    public void deleteRefreshToken(Long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }
}
