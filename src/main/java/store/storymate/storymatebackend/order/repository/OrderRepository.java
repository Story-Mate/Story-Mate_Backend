package store.storymate.storymatebackend.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.storymate.storymatebackend.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
