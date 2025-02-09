package store.storymate.storymatebackend.auth.dto;

public record RefreshTokenDto(Long memberId, String tokenValue, Long ttl) {}
