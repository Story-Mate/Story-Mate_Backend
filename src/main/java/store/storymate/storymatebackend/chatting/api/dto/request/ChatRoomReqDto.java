package store.storymate.storymatebackend.chatting.api.dto.request;

import store.storymate.storymatebackend.global.entity.Status;

public record ChatRoomReqDto(
        String roomName,
        Status status,
        String title,
        Integer liking,
        Long toMemberId
) {
}
