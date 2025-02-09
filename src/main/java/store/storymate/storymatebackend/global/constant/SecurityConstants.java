package store.storymate.storymatebackend.global.constant;

public final class SecurityConstants {

    // kakao
    public static final String KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me";

    // security
    public static final String TOKEN_ROLE_NAME = "role";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";

    private SecurityConstants() {}
}
