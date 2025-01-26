package store.storymate.storymatebackend.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.auth.api.dto.request.RefreshTokenRequestDto;
import store.storymate.storymatebackend.auth.api.dto.response.MemberLoginResponseDto;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;
import store.storymate.storymatebackend.global.jwt.TokenProvider;
import store.storymate.storymatebackend.global.jwt.api.dto.TokenDto;
import store.storymate.storymatebackend.global.jwt.domain.Token;
import store.storymate.storymatebackend.global.jwt.domain.repository.TokenRepository;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TokenDto getToken(MemberLoginResponseDto memberLoginResponseDto) {
        TokenDto tokenDTO = tokenProvider.generateToken(memberLoginResponseDto.findMember().getEmail(),
                memberLoginResponseDto.findMember().getRole());

        tokenSaveAndUpdate(memberLoginResponseDto, tokenDTO);

        return tokenDTO;
    }

    private void tokenSaveAndUpdate(MemberLoginResponseDto memberLoginResponseDto, TokenDto tokenDTO) {
        if (!tokenRepository.existsByMember(memberLoginResponseDto.findMember())) {
            tokenRepository.save(Token.builder()
                    .member(memberLoginResponseDto.findMember())
                    .refreshToken(tokenDTO.refreshToken())
                    .build());
        }

        refreshTokenUpdate(memberLoginResponseDto, tokenDTO);
    }

    private void refreshTokenUpdate(MemberLoginResponseDto memberLoginResponseDto, TokenDto tokenDTO) {
        Token token = tokenRepository.findByMember(memberLoginResponseDto.findMember()).orElseThrow();
        token.refreshTokenUpdate(tokenDTO.refreshToken());
    }

    @Transactional
    public TokenDto generateAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        if (isInvalidRefreshToken(refreshTokenRequestDto.refreshToken())) {
            throw new BadRequestException("유효하지 않은 refresh token 입니다.");
        }

        Token token = tokenRepository.findByRefreshToken(refreshTokenRequestDto.refreshToken()).orElseThrow();
        Member member = memberRepository.findById(token.getMember().getId()).orElseThrow();

        return tokenProvider.generateAccessTokenByRefreshToken(member.getEmail(), token.getRefreshToken());
    }

    private boolean isInvalidRefreshToken(String refreshToken) {
        return !tokenRepository.existsByRefreshToken(refreshToken) || !tokenProvider.validateToken(refreshToken);
    }
}
