package store.storymate.storymatebackend.reading.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.storymate.storymatebackend.global.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int position;

    @ManyToOne
    @JoinColumn(name = "member_book_id", nullable = false)
    private MemberBook memberBook;

    @Builder
    public Bookmark(
            int position,
            MemberBook memberBook
    ) {
        this.position = position;
        this.memberBook = memberBook;
    }
}
