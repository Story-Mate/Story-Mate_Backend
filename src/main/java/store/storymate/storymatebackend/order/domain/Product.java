package store.storymate.storymatebackend.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    private Long id;
    private String name;
    private Integer price;

    @Column(name = "message_count")
    private Long messageCount;

    @Builder(access = AccessLevel.PRIVATE)
    private Product(String name, Integer price, Long messageCount) {
        this.name = name;
        this.price = price;
        this.messageCount = messageCount;
    }
}
