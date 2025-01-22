package store.storymate.storymatebackend.chatting.api.dto.request;

import java.time.LocalDateTime;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

public record ChatMessageReqDto(
        String roomId,
        String sender,
        String content,
        LocalDateTime timestamp
) {
    public static ChatMessage toEntity(ChatRoom chatRoom, String sender, String content, LocalDateTime timestamp) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .timestamp(timestamp)
                .build();
    }
}

