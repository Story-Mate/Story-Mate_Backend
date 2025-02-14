package store.storymate.storymatebackend.reading.api.dto.request;

import lombok.Builder;
import store.storymate.storymatebackend.reading.domain.MemberBook;
import store.storymate.storymatebackend.reading.domain.Note;

@Builder
public record NoteCreateRequest(
        int position,
        String content
) {
    public static Note toEntity(MemberBook memberBook, NoteCreateRequest noteCreateRequest) {
        return Note.builder()
                .memberBook(memberBook)
                .position(noteCreateRequest.position())
                .content(noteCreateRequest.content())
                .build();
    }
}
