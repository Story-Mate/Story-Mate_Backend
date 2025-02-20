package store.storymate.storymatebackend.payment.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.payment.application.KakaoPayService;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayApproveRequest;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayPrepareRequest;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayApproveResponse;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayPrepareResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment/kakao-pay")
public class KakaoPayController implements KakaoPayDocs {
    private final KakaoPayService kakaoPayService;

    @PostMapping("/prepare")
    public ApiResponseTemplate<KakaoPayPrepareResponse> preparePayment(@RequestBody KakaoPayPrepareRequest request) {
        return ApiResponseTemplate.ok(kakaoPayService.kakaoPayReady(request));
    }

    @PostMapping("/approve")
    public ApiResponseTemplate<KakaoPayApproveResponse> approvePayment(@RequestParam("pg_token") String pgToken,
                                                                       @RequestBody KakaoPayApproveRequest request) {
        KakaoPayApproveResponse kakaoPayApproveResponse = kakaoPayService.kakaoPayApprove(pgToken, request);

        return ApiResponseTemplate.ok(kakaoPayApproveResponse);
    }
}

