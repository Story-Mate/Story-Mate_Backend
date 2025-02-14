package store.storymate.storymatebackend.reading.api.dto.response;

import store.storymate.storymatebackend.reading.domain.Highlight;

public record HighlightResponse(Long id, String paragraph, int startPosition, int endPosition) {
    public static HighlightResponse fromEntity(Highlight highlight) {
        return new HighlightResponse(highlight.getId(), highlight.getParagraph(), highlight.getStartPosition(), highlight.getEndPosition());
    }
} 