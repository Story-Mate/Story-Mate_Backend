package store.storymate.storymatebackend.payment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayApproveRequest;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayPrepareRequest;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayApproveResponse;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayPrepareResponse;

@Tag(name = "[카카오페이 결제 API]", description = "카카오페이 결제 관련 API")
public interface KakaoPayDocs {

    @Operation(summary = "카카오페이 결제 준비", description = "카카오페이 결제를 위한 준비 요청을 보냅니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 준비 성공",
                            content = @Content(schema = @Schema(implementation = KakaoPayPrepareResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<KakaoPayPrepareResponse> preparePayment(
            @Parameter(description = "상품 ID", required = true) KakaoPayPrepareRequest request);

    @Operation(summary = "카카오페이 결제 승인", description = "카카오페이 결제 요청 후, 사용자가 결제를 승인하면 이를 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 승인 성공",
                            content = @Content(schema = @Schema(implementation = KakaoPayApproveResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<KakaoPayApproveResponse> approvePayment(
            @Parameter(description = "결제 승인 토큰", required = true) String pgToken,
            @Parameter(description = "결제 고유 번호", required = true)KakaoPayApproveRequest request
            );
}
