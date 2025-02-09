package store.storymate.storymatebackend.member.dto;


public record CreateMemberRequest(
        String nickname,
        String profileImageUrl) {
}
