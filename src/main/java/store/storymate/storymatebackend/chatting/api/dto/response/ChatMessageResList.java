package store.storymate.storymatebackend.chatting.api.dto.response;

import java.util.List;
import lombok.Builder;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;

@Builder
public record ChatMessageResList(
        List<ChatMessageResDto> chatMessages,
        PageInfoResDto pageInfo
) {
    public static ChatMessageResList of(List<ChatMessageResDto> messages, PageInfoResDto pageInfo) {
        return ChatMessageResList.builder()
                .chatMessages(messages)
                .pageInfo(pageInfo)
                .build();
    }
}
