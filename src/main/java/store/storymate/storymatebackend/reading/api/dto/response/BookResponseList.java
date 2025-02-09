package store.storymate.storymatebackend.reading.api.dto.response;

import java.util.List;
import lombok.Builder;
import store.storymate.storymatebackend.reading.api.dto.PageInfoDto;

@Builder
public record BookResponseList(
        List<BookResponse> content,
        PageInfoDto pageInfo
) {
    public static BookResponseList of(List<BookResponse> content, PageInfoDto pageInfo) {
        return BookResponseList.builder()
                .content(content)
                .pageInfo(pageInfo)
                .build();
    }
}
