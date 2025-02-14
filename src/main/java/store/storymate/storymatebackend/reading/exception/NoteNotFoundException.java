package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class NoteNotFoundException extends NotFoundException {
    public NoteNotFoundException() {
        super("해당하는 노트를 찾을 수 없습니다.");
    }
} 