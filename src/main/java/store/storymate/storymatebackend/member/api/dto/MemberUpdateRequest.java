package store.storymate.storymatebackend.member.api.dto;

public record MemberUpdateRequest(
        String nickname,
        String birthDate
) {
}
