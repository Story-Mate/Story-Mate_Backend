package store.storymate.storymatebackend.reading.api.dto.request;

public record NoteUpdateRequest(
        Long noteId,
        String content
) {
}
