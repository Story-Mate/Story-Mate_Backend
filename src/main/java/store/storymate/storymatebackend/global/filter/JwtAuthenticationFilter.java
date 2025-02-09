package store.storymate.storymatebackend.global.filter;

import static store.storymate.storymatebackend.global.constant.SecurityConstants.ACCESS_TOKEN_COOKIE_NAME;
import static store.storymate.storymatebackend.global.constant.SecurityConstants.REFRESH_TOKEN_COOKIE_NAME;
import static store.storymate.storymatebackend.global.constant.SecurityConstants.TOKEN_PREFIX;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import store.storymate.storymatebackend.auth.dto.AccessTokenDto;
import store.storymate.storymatebackend.auth.dto.RefreshTokenDto;
import store.storymate.storymatebackend.global.jwt.JwtTokenProvider;
import store.storymate.storymatebackend.global.util.CookieUtil;
import store.storymate.storymatebackend.member.domain.MemberRole;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;

    private static String extractAccessTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(header -> header.replace(TOKEN_PREFIX, ""))
                .orElse(null);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String accessTokenHeaderValue = extractAccessTokenFromHeader(request);
        String accessTokenValue = extractAccessTokenFromCookie(request);
        String refreshTokenValue = extractRefreshTokenFromCookie(request);

        // 헤더에 AT가 있으면 우선적으로 검증
        if (accessTokenHeaderValue != null) {
            AccessTokenDto accessTokenDto =
                    jwtTokenProvider.retrieveAccessToken(accessTokenHeaderValue);
            if (accessTokenDto != null) {
                setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.memberRole());
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 쿠키에서 가져올 때 AT와 RT 중 하나라도 없으면 실패
        if (accessTokenValue == null || refreshTokenValue == null) {
            filterChain.doFilter(request, response);
            return;
        }

        AccessTokenDto accessTokenDto = jwtTokenProvider.retrieveAccessToken(accessTokenValue);

        if (accessTokenDto != null) {
            setAuthenticationToContext(accessTokenDto.memberId(), accessTokenDto.memberRole());
            filterChain.doFilter(request, response);
            return;
        }

        Optional<AccessTokenDto> reissuedAccessToken =
                Optional.ofNullable(jwtTokenProvider.reissueAccessTokenIfExpired(accessTokenValue));
        RefreshTokenDto refreshTokenDto = jwtTokenProvider.retrieveRefreshToken(refreshTokenValue);

        if (reissuedAccessToken.isPresent() && refreshTokenDto != null) {
            AccessTokenDto accessToken = reissuedAccessToken.get(); // 재발급된 AT
            RefreshTokenDto refreshToken =
                    jwtTokenProvider.createRefreshTokenDto(refreshTokenDto.memberId());

            HttpHeaders httpHeaders =
                    cookieUtil.generateTokenCookies(
                            accessToken.tokenValue(), refreshToken.tokenValue());
            response.addHeader(
                    HttpHeaders.SET_COOKIE, httpHeaders.getFirst(ACCESS_TOKEN_COOKIE_NAME));
            response.addHeader(
                    HttpHeaders.SET_COOKIE, httpHeaders.getFirst(REFRESH_TOKEN_COOKIE_NAME));

            setAuthenticationToContext(accessToken.memberId(), accessToken.memberRole());
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(Long memberId, MemberRole memberRole) {
        UserDetails userDetails = new PrincipalDetails(memberId, memberRole);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractAccessTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, ACCESS_TOKEN_COOKIE_NAME))
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, REFRESH_TOKEN_COOKIE_NAME))
                .map(Cookie::getValue)
                .orElse(null);
    }
}
