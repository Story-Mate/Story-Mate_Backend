package store.storymate.storymatebackend.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoPayApproveResponse(
        String aid,
        String tid,
        String cid,
        String sid,
        String partnerOrderId,
        String partnerUserId,
        String paymentMethodType,
        Amount amount,
        CardInfo cardInfo,
        String itemName,
        String itemCode,
        Integer quantity,
        String createdAt,
        String approvedAt
) {
    public record Amount(
            Integer total,
            Integer taxFree,
            Integer vat,
            Integer point,
            Integer discount
    ) {
    }

    public record CardInfo(
            String purchaseCorporation,
            String purchaseMethod
    ) {
    }
}
