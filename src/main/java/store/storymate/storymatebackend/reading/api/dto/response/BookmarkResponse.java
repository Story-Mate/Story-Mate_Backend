package store.storymate.storymatebackend.reading.api.dto.response;

import store.storymate.storymatebackend.reading.domain.Bookmark;

public record BookmarkResponse(Long id, int position) {
    public static BookmarkResponse fromEntity(Bookmark bookmark) {
        return new BookmarkResponse(bookmark.getId(), bookmark.getPosition());
    }
} 