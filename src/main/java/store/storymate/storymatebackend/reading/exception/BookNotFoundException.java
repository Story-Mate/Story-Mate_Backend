package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
