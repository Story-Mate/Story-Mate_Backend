package store.storymate.storymatebackend.global.error.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record ErrorResponse(
        String message
) {
   public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }
}
