package store.storymate.storymatebackend.quiz.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizAnswerReqDto;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizQuestionReqDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizAnswerResDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizQuestionResDto;
import store.storymate.storymatebackend.quiz.application.QuizService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/question")
    public ApiResponseTemplate<QuizQuestionResDto> getAiQuestion(
            @RequestBody QuizQuestionReqDto quizQuestionReqDto) {
        return ApiResponseTemplate.ok("퀴즈 생성 성공", quizService.callAiQuestionApi(quizQuestionReqDto));
    }

    @GetMapping("/answer")
    public ApiResponseTemplate<QuizAnswerResDto> getAiAnswer(
            @RequestBody QuizAnswerReqDto quizAnswerReqDto) {
        return ApiResponseTemplate.ok("정답 생성 성공", quizService.callAiAnswerApi(quizAnswerReqDto));
    }
}
