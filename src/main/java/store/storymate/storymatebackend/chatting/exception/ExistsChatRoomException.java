package store.storymate.storymatebackend.chatting.exception;


import store.storymate.storymatebackend.global.error.exception.NotFoundException;

public class ExistsChatRoomException extends NotFoundException {
    public ExistsChatRoomException(String message) {
        super(message);
    }

    public ExistsChatRoomException() {
        this("채팅방이 존재하지 않습니다.");
    }
}