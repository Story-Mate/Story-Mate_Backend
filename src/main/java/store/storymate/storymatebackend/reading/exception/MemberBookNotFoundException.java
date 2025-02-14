package store.storymate.storymatebackend.reading.exception;

import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class MemberBookNotFoundException extends NotFoundException {
    public MemberBookNotFoundException() {
        super("작품 감상 기록을 찾을 수 없습니다.");
    }
} 