package store.storymate.storymatebackend.quiz.api.dto.request;

public record QuizQuestionReqDto(
        String characterName,
        String bookTitle,
        String quizType
) {
}
