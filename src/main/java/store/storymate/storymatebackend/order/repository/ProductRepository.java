package store.storymate.storymatebackend.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.storymate.storymatebackend.order.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
