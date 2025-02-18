package store.storymate.storymatebackend.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.storymate.storymatebackend.auth.domain.OAuthProvider;
import store.storymate.storymatebackend.global.domain.BaseEntity;
import store.storymate.storymatebackend.global.domain.Status;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OauthInfo oauthInfo;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private int age;

    private LocalDate birthDate;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "message_count")
    private Long messageCount;

    @Column(name = "invite_code")
    private String inviteCode;

    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(OauthInfo oauthInfo, String nickname, MemberRole memberRole, int age,
                   String profileImageUrl, Long messageCount, String inviteCode, Status status) {
        this.oauthInfo = oauthInfo;
        this.memberRole = memberRole;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.messageCount = messageCount;
        this.inviteCode = inviteCode;
        this.status = status;
    }

    public static Member createMember(OAuthProvider oAuthProvider,
                                      String oauthId,
                                      String email,
                                      String nickname,
                                      String profileImageUrl) {
        OauthInfo oauthInfo = OauthInfo.createOauthInfo(oauthId, oAuthProvider.getValue(), email, nickname);

        return Member.builder()
                .oauthInfo(oauthInfo)
                .nickname(nickname)
                .memberRole(MemberRole.USER)
                .profileImageUrl(profileImageUrl)
                .messageCount(10L)
                .inviteCode(InviteCodeGenerator.generateInviteCode())
                .status(Status.ACTIVE)
                .build();
    }

    private static int calculateAgeFromBirthYear(String birthDate) {
        int currentYear = LocalDate.now().getYear();
        int birthYearInt = LocalDate.of(Integer.parseInt(birthDate), 1, 1).getYear();

        return currentYear - birthYearInt;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateBirthDate(String birthDate) {
        this.birthDate = LocalDate.of(Integer.parseInt(birthDate), 1, 1);
        this.age = calculateAgeFromBirthYear(birthDate);
    }
}
