package store.storymate.storymatebackend.chatting.api.dto.request;

import lombok.Builder;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.reading.domain.Book;

@Builder
public record ChatMessageSaveReqDto(
        String roomId,
        String sender,
        String content,
        String bookTitle
) {
}
