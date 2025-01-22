package store.storymate.storymatebackend.chatting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String content;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(String sender, String content, ChatRoom chatRoom, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.chatRoom = chatRoom;
        this.timestamp = timestamp;
    }


    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id != null ? id.toString() : null);
        map.put("sender", sender);
        map.put("content", content);
        map.put("timestamp", timestamp != null ? timestamp.toString() : null);
        map.put("chatRoomId", chatRoom != null ? chatRoom.getId().toString() : null);
        return map;
    }

    public static ChatMessage fromMap(Map<String, String> map, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .sender(map.get("sender"))
                .content(map.get("content"))
                .chatRoom(chatRoom) // ChatRoom은 외부에서 주입
                .timestamp(LocalDateTime.parse(map.get("timestamp"))) // 타임스탬프 파싱
                .build();
    }
}