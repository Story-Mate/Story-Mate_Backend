package store.storymate.storymatebackend.chatting.exception;

import store.storymate.storymatebackend.global.error.exception.BadRequestException;

public class AiChatRoomException extends BadRequestException {
    public AiChatRoomException(String message) {
        super(message);
    }

    public AiChatRoomException() {
        this("AI 서버 응답에 실패했습니다.");
    }
}