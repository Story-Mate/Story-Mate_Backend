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
import store.storymate.storymatebackend.quiz.api.dto.request.QuizQuestionReqDto;
import store.storymate.storymatebackend.quiz.exception.AiQuizQuestionException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private WebClient webClient;

    @Value("${ai.characters}")
    private String baseUrl;

    @PostConstruct
    private void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // AI 문제 받는 기능
    public String callAiQuestionApi(QuizQuestionReqDto quizQuestionReqDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("character_name", quizQuestionReqDto.characterName());
        requestBody.put("question_number", quizQuestionReqDto.questionNumber());
        requestBody.put("novel_title", quizQuestionReqDto.novelTitle());

        String encodedUri = UriComponentsBuilder.fromPath("/")
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
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("response"))
                .block();
    }
    // AI 답변 받는 기능
}
