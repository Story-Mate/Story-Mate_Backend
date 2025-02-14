package store.storymate.storymatebackend.reading.api.dto.response;

import store.storymate.storymatebackend.reading.domain.Note;

public record NoteResponse(Long id, String content, int position) {
    public static NoteResponse fromEntity(Note note) {
        return new NoteResponse(note.getId(), note.getContent(), note.getPosition());
    }
} 