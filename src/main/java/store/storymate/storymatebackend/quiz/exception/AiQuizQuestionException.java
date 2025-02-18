package store.storymate.storymatebackend.quiz.exception;

import store.storymate.storymatebackend.global.error.exception.BadRequestException;

public class AiQuizQuestionException extends BadRequestException {
    public AiQuizQuestionException(String message) {
        super(message);
    }

    public AiQuizQuestionException() {
        this("AI 퀴즈 생성에 실패했습니다.");
    }
}