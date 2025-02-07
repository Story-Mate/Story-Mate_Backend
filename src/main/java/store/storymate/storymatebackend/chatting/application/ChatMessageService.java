package store.storymate.storymatebackend.chatting.application;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.chatting.domain.repository.ChatMessageRepository;
import store.storymate.storymatebackend.chatting.domain.repository.ChatRoomRepository;
import store.storymate.storymatebackend.chatting.exception.ExistsChatRoomException;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://ai.dev.storymate.site:5000")  // 기본 URL 설정
            .build();

    @Transactional
    public void saveMessage(String roomId, String sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                .orElseThrow(ExistsChatRoomException::new);

        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);
    }

    public String callAiApi(String sessionId, String characterName, String query) {
        // 요청 데이터 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("session_id", sessionId);
        requestBody.put("character_name", characterName);
        requestBody.put("query", query);

        try {
            // 경로에 포함된 한글이나 특수문자를 인코딩하여 URL을 안전하게 생성
            String encodedUri = UriComponentsBuilder.fromPath("/")
                    .encode()
                    .toUriString();

            // WebClient를 이용한 POST 요청
            return webClient.post()
                    .uri(encodedUri)  // 안전하게 인코딩된 URI 사용
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,  // 상태 코드가 에러인지 확인
                            clientResponse -> Mono.error(new RuntimeException("AI 서버 오류 발생")))
                    .bodyToMono(Map.class)
                    .map(response -> (String) response.get("response"))  // 응답에서 "response" 추출
                    .block();  // 동기적으로 결과 대기

        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI 서버 응답 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

    public ChatMessageResList findChatMessages(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ExistsChatRoomException::new);

        Page<ChatMessage> messagePage = chatMessageRepository.findByChatRoom(chatRoom, pageable);
        List<ChatMessage> messages = messagePage.getContent();

        List<ChatMessageResDto> messageDtos = messages.stream()
                .map(ChatMessageResDto::fromEntity)
                .collect(Collectors.toList());

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .totalPages(messagePage.getTotalPages())
                .totalItems(messagePage.getTotalElements())
                .currentPage(pageable.getPageNumber())
                .build();

        return ChatMessageResList.of(messageDtos, pageInfoResDto);
    }
}
