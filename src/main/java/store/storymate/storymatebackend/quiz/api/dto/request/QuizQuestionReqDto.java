package store.storymate.storymatebackend.quiz.api.dto.request;

public record QuizQuestionReqDto(
        String characterName,
        String quizType,
        String bookTitle
) {
}
