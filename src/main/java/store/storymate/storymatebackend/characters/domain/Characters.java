package store.storymate.storymatebackend.characters.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.global.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class Characters extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private Integer like_count;

    @OneToMany(mappedBy = "characters", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoom = new ArrayList<>();

    @Builder
    public Characters(String description, String imageUrl, Integer likeCount, String name) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.like_count = likeCount;
        this.name = name;
    }
}
