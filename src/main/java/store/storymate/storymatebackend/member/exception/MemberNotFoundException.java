package store.storymate.storymatebackend.member.exception;


import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        this("존재하지 않는 회원입니다");
    }
}