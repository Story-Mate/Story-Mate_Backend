package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException() {
        super("해당하는 책을 찾을 수 없습니다.");
    }
}
