package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class BookmarkNotFoundException extends NotFoundException {
    public BookmarkNotFoundException() {
        super("해당하는 북마크를 찾을 수 없습니다.");
    }

}
