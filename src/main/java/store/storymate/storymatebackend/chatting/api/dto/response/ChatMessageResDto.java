package store.storymate.storymatebackend.chatting.api.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;

@Builder
public record ChatMessageResDto(
        String sender,
        String content,
        LocalDateTime timestamp
) {
    public static ChatMessageResDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .build();
    }
}