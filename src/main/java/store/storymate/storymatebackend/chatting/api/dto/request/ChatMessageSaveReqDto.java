package store.storymate.storymatebackend.chatting.api.dto.request;

import lombok.Builder;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

@Builder
public record ChatMessageSaveReqDto(
        String roomId,
        String sender,
        String content
) {
    public static ChatMessage toEntity(ChatRoom chatRoom, String sender, String content) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();
    }
}
