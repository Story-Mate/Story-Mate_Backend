package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class HighlightNotFoundException extends NotFoundException {
    public HighlightNotFoundException() {
        super("해당하는 하이라이트를 찾을 수 없습니다.");
    }
}
