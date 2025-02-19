package store.storymate.storymatebackend.member.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import store.storymate.storymatebackend.member.domain.Member;

@Builder
public record MemberResponse(
        String nickname,
        LocalDate birthDate,
        String profileImageUrl,
        Long messageCount,
        String inviteCode
) {
    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .nickname(member.getOauthInfo().getNickname())
                .birthDate(member.getBirthDate())
                .profileImageUrl(member.getProfileImageUrl())
                .messageCount(member.getMessageCount())
                .inviteCode(member.getInviteCode())
                .build();
    }
}
