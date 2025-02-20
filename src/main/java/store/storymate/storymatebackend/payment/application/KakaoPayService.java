package store.storymate.storymatebackend.payment.application;

import jakarta.transaction.Transactional;
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

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KakaoPayProperties.class)
public class KakaoPayService {
    private static final String BASE_URL = "https://open-api.kakaopay.com";
    private static final String PAYMENT_READY_ENDPOINT = "/online/v1/payment/ready";
    private static final String PAYMENT_APPROVE_ENDPOINT = "/online/v1/payment/approve";
    private static final String PAYMENT_CANCEL_ENDPOINT = "/online/v1/payment/cancel";  // TODO: 취소 기능 추후 구현 예정
    private static final String CID = "TC0ONETIME";

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final MemberUtil memberUtil;
    private final KakaoPayProperties kakaoPayProperties;
    private final RestClient restClient;

    @Transactional
    public KakaoPayPrepareResponse kakaoPayReady(KakaoPayPrepareRequest request) {
        try {
            Product product = getProduct(request.productId());
            Order order = createOrder(product);
            KakaoPayPrepareResponse prepareResponse = requestKakaoPayPrepare(order, product);
            savePayment(prepareResponse.tid(), order);

            return prepareResponse;
        } catch (Exception e) {
            log.error("KakaoPay ready failed", e);
            throw new BadRequestException("KakaoPay 결제 준비 요청 실패");
        }
    }

    @Transactional
    public KakaoPayApproveResponse kakaoPayApprove(String pgToken, KakaoPayApproveRequest request) {
        Payment payment = findPaymentByTid(request.tid());
        Order order = payment.getOrder();
        KakaoPayApproveResponse response = requestKakaoPayApprove(order, request.tid(), pgToken);

        updatePaymentStatus(payment);
        order.getMember().addMessageCount(order.getProduct().getMessageCount());
        return response;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("해당 상품을 찾을 수 없습니다."));
    }

    private Order createOrder(Product product) {
        Order order = Order.builder()
                .product(product)
                .member(memberUtil.getCurrentMember())
                .status(OrderStatus.PENDING)
                .build();
        return orderRepository.save(order);
    }

    private KakaoPayPrepareResponse requestKakaoPayPrepare(Order order, Product product) {
        return restClient.post()
                .uri(BASE_URL + PAYMENT_READY_ENDPOINT)
                .headers(this::setKakaoPayHeaders)
                .body(createPrepareParams(order, product))
                .retrieve()
                .body(KakaoPayPrepareResponse.class);
    }

    private void savePayment(String tid, Order order) {
        Payment payment = Payment.builder()
                .tid(tid)
                .order(order)
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);
    }

    private Payment findPaymentByTid(String tid) {
        return paymentRepository.findByTid(tid)
                .orElseThrow(() -> new BadRequestException("해당 TID의 결제 정보를 찾을 수 없습니다."));
    }

    private KakaoPayApproveResponse requestKakaoPayApprove(Order order, String tid, String pgToken) {
        return restClient.post()
                .uri(BASE_URL + PAYMENT_APPROVE_ENDPOINT)
                .headers(this::setKakaoPayHeaders)
                .body(createApproveParams(order, tid, pgToken))
                .retrieve()
                .body(KakaoPayApproveResponse.class);
    }

    private void updatePaymentStatus(Payment payment) {
        payment.approve();
        payment.getOrder().complete();
        paymentRepository.save(payment);
    }

    private void setKakaoPayHeaders(HttpHeaders headers) {
        headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.secretKey());
        headers.set("Content-Type", "application/json");
    }

    private Map<String, Object> createPrepareParams(Order order, Product product) {
        return Map.of(
                "cid", CID,
                "partner_order_id", order.getId(),
                "partner_user_id", memberUtil.getCurrentMember().getId(),
                "item_name", product.getName(),
                "quantity", 1,
                "total_amount", product.getPrice(),
                "tax_free_amount", 0,
                "approval_url", kakaoPayProperties.approvalUrl(),
                "cancel_url", kakaoPayProperties.cancelUrl(),
                "fail_url", kakaoPayProperties.failUrl()
        );
    }

    private Map<String, Object> createApproveParams(Order order, String tid, String pgToken) {
        return Map.of(
                "cid", CID,
                "tid", tid,
                "partner_order_id", order.getId(),
                "partner_user_id", memberUtil.getCurrentMember().getId(),
                "pg_token", pgToken
        );
    }
}
