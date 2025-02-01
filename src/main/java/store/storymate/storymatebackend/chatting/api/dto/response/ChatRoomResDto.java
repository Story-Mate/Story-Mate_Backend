package store.storymate.storymatebackend.chatting.api.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Builder;

@Builder
public record ChatRoomResDto(
        Long roomId,
        String title,
        String loginUserName,
        Integer liking,
        String memberImage,
        String charactersName,
        String charactersImage
) {
    public static ChatRoomResDto from(Long roomId,
                                      String title,
                                      Integer liking,
                                      String loginUserName,
                                      String memberImage,
                                      String charactersName,
                                      String charactersImage) {
        return ChatRoomResDto.builder()
                .roomId(roomId)
                .title(title)
                .liking(liking)
                .loginUserName(loginUserName)
                .memberImage(memberImage)
                .charactersName(charactersName)
                .charactersImage(charactersImage)
                .build();
    }
}
