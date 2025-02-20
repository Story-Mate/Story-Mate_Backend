package store.storymate.storymatebackend.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoPayPrepareResponse(
        String tid,
        String nextRedirectPcUrl,
        String nextRedirectMobileUrl,
        String nextRedirectAppUrl,
        String createdAt
) {
}
