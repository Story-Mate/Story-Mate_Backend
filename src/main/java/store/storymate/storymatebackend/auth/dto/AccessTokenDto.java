package store.storymate.storymatebackend.auth.dto;


import store.storymate.storymatebackend.member.domain.MemberRole;

public record AccessTokenDto(Long memberId, MemberRole memberRole, String tokenValue) {
}
