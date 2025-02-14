package store.storymate.storymatebackend.reading.api.dto.request;

import store.storymate.storymatebackend.reading.domain.Highlight;
import store.storymate.storymatebackend.reading.domain.MemberBook;

public record HighlightCreateRequest(
        int startPosition,
        int endPosition,
        String paragraph
) {
    public static Highlight toEntity(MemberBook memberBook, HighlightCreateRequest highlightCreateRequest) {
        return Highlight.builder()
                .memberBook(memberBook)
                .paragraph(highlightCreateRequest.paragraph())
                .startPosition(highlightCreateRequest.startPosition())
                .endPosition(highlightCreateRequest.endPosition())
                .build();
    }
}
