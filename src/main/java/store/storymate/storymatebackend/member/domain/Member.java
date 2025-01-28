package store.storymate.storymatebackend.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.storymate.storymatebackend.global.domain.BaseEntity;
import store.storymate.storymatebackend.global.domain.Status;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Enumerated(value = EnumType.STRING)
    private Role role;


    private int age;

    @Column(name = "message_count")
    private Long messageCount;

    @Column(name = "invite_code")
    private String inviteCode;

    private boolean firstLogin;

    private Status status;

    @Builder
    public Member(String email, String name, String profileImageUrl, SocialType socialType, int age,
                   Long messageCount, boolean firstLogin, Status status) {
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.socialType = socialType;
        this.role = Role.ROLE_USER;
        this.age = age;
        this.messageCount = 0L;
        this.inviteCode = InviteCodeGenerator.generateInviteCode();
        this.firstLogin = firstLogin;
        this.status = status;
    }
}
