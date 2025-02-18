package store.storymate.storymatebackend.chatting.application;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatMessageSaveReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.chatting.domain.repository.ChatMessageRepository;
import store.storymate.storymatebackend.chatting.domain.repository.ChatRoomRepository;
import store.storymate.storymatebackend.chatting.exception.AiChatRoomException;
import store.storymate.storymatebackend.chatting.exception.ExistsChatRoomException;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;
import store.storymate.storymatebackend.reading.exception.BookNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BookRepository bookRepository;
    private WebClient webClient;

    @Value("${ai.characters}")
    private String baseUrl;

    @PostConstruct
    private void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Transactional
    public void saveMessage(ChatMessageSaveReqDto chatMessageSaveReqDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(chatMessageSaveReqDto.roomId()))
                .orElseThrow(ExistsChatRoomException::new);

        ChatMessage chatMessage = ChatMessage.builder()
                .sender(chatMessageSaveReqDto.sender())
                .content(chatMessageSaveReqDto.content())
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);
    }

    public String callAiApi(ChatMessageSaveReqDto chatMessageSaveReqDto) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("session_id", chatMessageSaveReqDto.roomId());
        requestBody.put("character_name", chatMessageSaveReqDto.sender());
        requestBody.put("query", chatMessageSaveReqDto.content());
        requestBody.put("book_title", chatMessageSaveReqDto.bookTitle());

        String encodedUri = UriComponentsBuilder.fromPath("/")
                .encode()
                .toUriString();

        return webClient.post()
                .uri(encodedUri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(AiChatRoomException::new)
                                .flatMap(Mono::error)
                )
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("response"))
                .block();
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
