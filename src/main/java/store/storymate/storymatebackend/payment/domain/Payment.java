package store.storymate.storymatebackend.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.storymate.storymatebackend.order.domain.Order;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Builder
    public Payment(String tid, Order order, PaymentStatus status) {
        this.tid = tid;
        this.order = order;
        this.status = status;
    }

    public void approve() {
        this.status = PaymentStatus.APPROVED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
    }
}

