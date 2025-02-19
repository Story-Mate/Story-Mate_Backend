package store.storymate.storymatebackend.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {

    @Column(name = "oauth_id")
    public String oauthId;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;


    @Builder(access = AccessLevel.PRIVATE)
    private OauthInfo(String oauthId, String oauthProvider, String email, String nickname) {
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.email = email;
        this.nickname = nickname;
    }

    public static OauthInfo createOauthInfo(
            String oauthId, String oauthProvider, String email, String nickname) {
        return OauthInfo.builder()
                .oauthId(oauthId)
                .oauthProvider(oauthProvider)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
