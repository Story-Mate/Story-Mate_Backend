package store.storymate.storymatebackend.auth.api.dto.response;


import lombok.Builder;
import store.storymate.storymatebackend.member.domain.Member;

@Builder
public record MemberLoginResponseDto(
        Member findMember
) {
    public static MemberLoginResponseDto from(Member member) {
        return MemberLoginResponseDto.builder()
                .findMember(member)
                .build();
    }
}
