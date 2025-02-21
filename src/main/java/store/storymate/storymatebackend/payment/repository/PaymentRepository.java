package store.storymate.storymatebackend.payment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.storymate.storymatebackend.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTid(String tid);
}

