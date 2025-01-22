package store.storymate.storymatebackend.chatting.api.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomResDto(
        Long roomId,
        String name,
        Long fromMemberId,
        Long toMemberId,
        String loginUserName,
        String recentMessage,
        Integer unreadNotification,
        String memberImage
) {
    public static ChatRoomResDto from(Long roomId,
                                      String name,
                                      Long fromMemberId,
                                      Long toMemberId,
                                      String loginUserName,
                                      String recentMessage,
                                      Integer unreadNotification,
                                      String memberImage) {
        return ChatRoomResDto.builder()
                .roomId(roomId)
                .name(name)
                .fromMemberId(fromMemberId)
                .toMemberId(toMemberId)
                .loginUserName(loginUserName)
                .recentMessage(recentMessage)
                .unreadNotification(unreadNotification)
                .memberImage(memberImage)
                .build();
    }
}
