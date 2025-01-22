package store.storymate.storymatebackend.chatting.api.dto.response;

import java.util.List;
import lombok.Builder;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;

@Builder
public record ChatRoomResList(
        List<ChatRoomResDto> chatRoomResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static ChatRoomResList of(List<ChatRoomResDto> chatRoomResDtos, PageInfoResDto pageInfoResDto) {
        return ChatRoomResList.builder()
                .chatRoomResDtos(chatRoomResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
