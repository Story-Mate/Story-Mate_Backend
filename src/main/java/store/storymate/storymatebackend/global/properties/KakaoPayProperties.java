package store.storymate.storymatebackend.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao-pay")
public record KakaoPayProperties(
        String secretKey,
        String cid,
        String approvalUrl,
        String cancelUrl,
        String failUrl
) {
}
