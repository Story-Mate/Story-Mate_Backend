package store.storymate.storymatebackend.payment.application;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;
import store.storymate.storymatebackend.global.properties.KakaoPayProperties;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.order.domain.Order;
import store.storymate.storymatebackend.order.domain.OrderStatus;
import store.storymate.storymatebackend.order.domain.Product;
import store.storymate.storymatebackend.order.repository.OrderRepository;
import store.storymate.storymatebackend.order.repository.ProductRepository;
import store.storymate.storymatebackend.payment.domain.Payment;
import store.storymate.storymatebackend.payment.domain.PaymentStatus;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayApproveRequest;
import store.storymate.storymatebackend.payment.dto.request.KakaoPayPrepareRequest;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayApproveResponse;
import store.storymate.storymatebackend.payment.dto.response.KakaoPayPrepareResponse;
import store.storymate.storymatebackend.payment.repository.PaymentRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(KakaoPayProperties.class)
public class KakaoPayService {
    private static final String BASE_URL = "https://open-api.kakaopay.com";
    private static final String KAKAO_PAY_READY = "/online/v1/payment/ready";
    private static final String KAKAO_PAY_APPROVE = "/online/v1/payment/approve";
    private static final String KAKAO_PAY_CANCEL = "/online/v1/payment/cancel";  // TODO: 취소 기능 추후 구현 예정

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final MemberUtil memberUtil;
    private final KakaoPayProperties kakaoPayProperties;
    private final RestClient restClient;
    private KakaoPayPrepareResponse kakaoPayPrepareResponse;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.secretKey());
        headers.set("Content-Type", "application/json");
        return headers;
    }

    @Transactional
    public KakaoPayPrepareResponse kakaoPayReady(KakaoPayPrepareRequest request) {
        try {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new BadRequestException("해당 상품을 찾을 수 없습니다."));

            Order order = Order.builder()
                    .product(product)
                    .member(memberUtil.getCurrentMember())
                    .status(OrderStatus.PENDING)
                    .build();
            orderRepository.save(order);

            Map<String, Object> params = getKakaoPayReadyParams(order, product);
            HttpHeaders headers = getHeaders();

            kakaoPayPrepareResponse = restClient.post()
                    .uri(BASE_URL + KAKAO_PAY_READY)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(params)
                    .retrieve()
                    .body(KakaoPayPrepareResponse.class);

            log.info("KakaoPay Ready Response: {}", kakaoPayPrepareResponse);

            Payment payment = Payment.builder()
                    .tid(kakaoPayPrepareResponse.tid())
                    .order(order)
                    .status(PaymentStatus.PENDING)
                    .build();
            paymentRepository.save(payment);

            return kakaoPayPrepareResponse;
        } catch (Exception e) {
            log.error("KakaoPay ready failed", e);
            throw new BadRequestException("KakaoPay 결제 준비 요청 실패");
        }
    }

    private Map<String, Object> getKakaoPayReadyParams(Order order, Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("partner_order_id", order.getId());
        params.put("partner_user_id", memberUtil.getCurrentMember().getId());
        params.put("item_name", product.getName());
        params.put("quantity", 1);
        params.put("total_amount", product.getPrice());
        params.put("tax_free_amount", 0);
        params.put("approval_url", kakaoPayProperties.approvalUrl());
        params.put("cancel_url", kakaoPayProperties.cancelUrl());
        params.put("fail_url", kakaoPayProperties.failUrl());
        return params;
    }

    @Transactional
    public KakaoPayApproveResponse kakaoPayApprove(String pgToken, KakaoPayApproveRequest request) {
        Payment payment = paymentRepository.findByTid(request.tid())
                .orElseThrow(() -> new BadRequestException("해당 TID의 결제 정보를 찾을 수 없습니다."));

        Map<String, Object> params = getKakaoPayApproveParams(payment.getOrder(), request.tid(), pgToken);
        HttpHeaders headers = getHeaders();

        KakaoPayApproveResponse response = restClient.post()
                .uri(BASE_URL + KAKAO_PAY_APPROVE)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(params)
                .retrieve()
                .body(KakaoPayApproveResponse.class);

        log.info("KakaoPay Approve Response: {}", response);

        payment.approve();
        payment.getOrder().complete();
        paymentRepository.save(payment);

        return response;

    }

    private Map<String, Object> getKakaoPayApproveParams(Order order, String tid, String pgToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", tid);
        params.put("partner_order_id", order.getId());
        params.put("partner_user_id", memberUtil.getCurrentMember().getId());
        params.put("pg_token", pgToken);
        return params;
    }
}

