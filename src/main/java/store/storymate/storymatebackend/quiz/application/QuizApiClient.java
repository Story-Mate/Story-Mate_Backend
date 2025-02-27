package store.storymate.storymatebackend.quiz.application;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizAnswerReqDto;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizQuestionReqDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizAnswerResDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizQuestionResDto;
import store.storymate.storymatebackend.quiz.exception.AiQuizQuestionException;

@Component
@RequiredArgsConstructor
public class QuizApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ai.characters}")
    private String baseUrl;

    private WebClient webClient;

    @PostConstruct
    private void initWebClient() {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public QuizQuestionResDto fetchQuizQuestion(QuizQuestionReqDto requestDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("character_name", requestDto.characterName());
        requestBody.put("quiz_type", requestDto.quizType());
        requestBody.put("book_title", requestDto.bookTitle());

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

    public QuizAnswerResDto evaluateQuizAnswer(QuizAnswerReqDto requestDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("book_title", requestDto.bookTitle());
        requestBody.put("character_name", requestDto.characterName());
        requestBody.put("quiz_type", requestDto.quizType());
        requestBody.put("user_answer", requestDto.userAnswer());

        String encodedUri = UriComponentsBuilder.fromPath("/evaluate_quiz")
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
                .bodyToMono(QuizAnswerResDto.class)
                .block();
    }
}
