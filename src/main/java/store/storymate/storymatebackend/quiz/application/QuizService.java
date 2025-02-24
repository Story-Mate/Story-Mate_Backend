package store.storymate.storymatebackend.quiz.application;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizAnswerReqDto;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizQuestionReqDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizAnswerResDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizQuestionResDto;
import store.storymate.storymatebackend.quiz.exception.AiQuizQuestionException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private WebClient webClient;
    private final MemberUtil memberUtil;

    @Value("${ai.characters}")
    private String baseUrl;

    @PostConstruct
    private void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // AI 문제 받는 기능
    public QuizQuestionResDto callAiQuestionApi(QuizQuestionReqDto quizQuestionReqDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("character_name", quizQuestionReqDto.characterName());
        requestBody.put("quiz_type", quizQuestionReqDto.quizType());
        requestBody.put("book_title", quizQuestionReqDto.bookTitle());

        String encodedUri = UriComponentsBuilder.fromPath("/quiz_question")
                .encode()
                .toUriString();

        return webClient.post()
                .uri(encodedUri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(AiQuizQuestionException::new)
                                .flatMap(Mono::error)
                )
                .bodyToMono(QuizQuestionResDto.class)
                .block();
    }

    @Transactional
    public QuizAnswerResDto callAiAnswerApi(QuizAnswerReqDto quizQuestionReqDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("book_title", quizQuestionReqDto.bookTitle());
        requestBody.put("character_name", quizQuestionReqDto.characterName());
        requestBody.put("quiz_type", quizQuestionReqDto.quizType());
        requestBody.put("user_answer", quizQuestionReqDto.userAnswer());

        String encodedUri = UriComponentsBuilder.fromPath("/evaluate_quiz")
                .encode()
                .toUriString();

        QuizAnswerResDto response = webClient.post()
                .uri(encodedUri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(AiQuizQuestionException::new)
                                .flatMap(Mono::error)
                )
                .bodyToMono(QuizAnswerResDto.class)
                .block();

        Member member = memberUtil.getCurrentMember();

        if (response != null && isCorrectAnswer(response.correct())) {
            member.addMessageCount(3L);
        }

        return response;
    }

    private boolean isCorrectAnswer(String correct) {
        return "O".equalsIgnoreCase(correct) || "C".equalsIgnoreCase(correct) || "true".equalsIgnoreCase(correct);
    }

}
