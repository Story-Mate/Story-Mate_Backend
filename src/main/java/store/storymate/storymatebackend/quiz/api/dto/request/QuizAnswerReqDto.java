package store.storymate.storymatebackend.quiz.api.dto.request;

public record QuizAnswerReqDto(
        String bookTitle,
        String characterName,
        String quizType,
        String userAnswer
) {
}
