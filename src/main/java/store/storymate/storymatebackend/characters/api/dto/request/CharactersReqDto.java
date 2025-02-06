package store.storymate.storymatebackend.characters.api.dto.request;

import java.time.LocalDateTime;
import store.storymate.storymatebackend.characters.domain.Characters;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

public record CharactersReqDto(
        String description,
        String imageUrl,
        Integer likeCount,
        String name
) {
    public static Characters toEntity(CharactersReqDto charactersReqDto) {
        return Characters.builder()
                .description(charactersReqDto.description())
                .imageUrl(charactersReqDto.imageUrl())
                .likeCount(charactersReqDto.likeCount())
                .name(charactersReqDto.name())
                .build();
    }
}

