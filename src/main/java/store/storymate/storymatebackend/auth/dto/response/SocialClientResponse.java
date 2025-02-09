package store.storymate.storymatebackend.auth.dto.response;

public record SocialClientResponse(String email, String oauthId, String nickname,
                                   String profileImage) {
}
