package store.storymate.storymatebackend.reading.api.dto.request;

import lombok.Builder;
import store.storymate.storymatebackend.reading.domain.Bookmark;
import store.storymate.storymatebackend.reading.domain.MemberBook;

@Builder
public record BookmarkCreateRequest(
        int position
) {
    public static Bookmark toEntity(MemberBook memberBook, BookmarkCreateRequest bookmarkCreateRequest) {
        return Bookmark.builder()
                .memberBook(memberBook)
                .position(bookmarkCreateRequest.position())
                .build();
    }
}
