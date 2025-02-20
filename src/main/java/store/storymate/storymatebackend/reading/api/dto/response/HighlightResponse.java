package store.storymate.storymatebackend.reading.api.dto.response;

import lombok.Builder;
import store.storymate.storymatebackend.reading.domain.Highlight;

@Builder
public record HighlightResponse(
        Long id,
        String paragraph,
        int startPosition,
        int endPosition,
        int pageNumber
) {
    public static HighlightResponse fromEntity(Highlight highlight) {
        return HighlightResponse.builder()
                .id(highlight.getId())
                .paragraph(highlight.getParagraph())
                .startPosition(highlight.getStartPosition())
                .endPosition(highlight.getEndPosition())
                .pageNumber(highlight.getPageNumber())
                .build();
    }
} 