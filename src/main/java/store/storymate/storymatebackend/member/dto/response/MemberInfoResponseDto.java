package store.storymate.storymatebackend.member.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import store.storymate.storymatebackend.member.domain.Member;

@Builder
public record MemberInfoResponseDto(
        Long id,
        String email,
        String nickname,
        int age,
        String profileImageUrl,
        Long messageCount,
        String inviteCode,
        LocalDate birthDate
) {
    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .email(member.getOauthInfo().getEmail())
                .nickname(member.getOauthInfo().getNickname())
                .age(member.getAge())
                .profileImageUrl(member.getProfileImageUrl())
                .messageCount(member.getMessageCount())
                .inviteCode(member.getInviteCode())
                .birthDate(member.getBirthDate())
                .build();
    }
}
