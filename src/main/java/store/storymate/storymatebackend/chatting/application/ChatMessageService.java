package store.storymate.storymatebackend.chatting.application;

import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.chatting.domain.repository.ChatMessageRepository;
import store.storymate.storymatebackend.chatting.domain.repository.ChatRoomRepository;
import store.storymate.storymatebackend.chatting.exception.ExistsChatRoomException;
import store.storymate.storymatebackend.redis.RedisService;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisService redisService; // Redis 연동
    private static final Duration REDIS_TTL = Duration.ofHours(1); // 1시간 TTL

    /**
     * 채팅 메시지를 redis에 저장 -> 구현 완료
     */
    @Transactional
    public void saveMessage(String roomId, String sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                .orElseThrow(ExistsChatRoomException::new);

        // 메시지를 Redis에 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();

        redisService.saveChatMessage(roomId, chatMessage.toMap().toString(), REDIS_TTL);
    }

    /**
     * 채팅 메시지 조회 (레디스 먼저 조회, 그 후 mysql 조회 -> 수정 중)
     */
    public ChatMessageResList findChatMessages(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ExistsChatRoomException::new);

        String redisKey = roomId.toString();

        // Redis에서 데이터 조회
        List<Map<String, String>> redisMessages = redisService.getChatMessages(redisKey);
        List<ChatMessage> messages = redisMessages.stream()
                .map(message -> ChatMessage.fromMap(parseToMap(message.toString()), chatRoom))
                .collect(Collectors.toList());
        System.out.println(messages);

        // Redis에 데이터가 없으면 DB 조회
        if (messages.isEmpty()) {
            Page<ChatMessage> messagePage = chatMessageRepository.findByChatRoom(chatRoom, pageable);
            messages = messagePage.getContent();
        }

        List<ChatMessageResDto> messageDtos = messages.stream()
                .map(ChatMessageResDto::fromEntity)
                .collect(Collectors.toList());

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .totalPages(1) // Redis에서는 페이지 지원이 제한적이므로 기본값 설정
                .totalItems(messages.size())
                .currentPage(0) // 기본 0번 페이지
                .build();

        return ChatMessageResList.of(messageDtos, pageInfoResDto);
    }

    private Map<String, String> parseToMap(String message) {
        Map<String, String> map = new HashMap<>();
        String[] entries = message.replace("{", "").replace("}", "").split(", ");

        for (String entry : entries) {
            String[] keyValue = entry.split("=", 2);
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return map;
    }

    /**
     * Redis에서 DB로 메시지 영구 저장 -> 구현 완료
     */
    @Transactional
    public void persistMessagesToDB() {
        // Redis에서 모든 방의 키 가져오기
        List<String> roomKeys = redisService.getKeys("chat:room:*");

        for (String roomKey : roomKeys) {
            String roomId = roomKey.replace("chat:room:", "");

            // MySQL에서 해당 채팅방 조회
            ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId))
                    .orElseThrow(() -> new ExistsChatRoomException("ChatRoom not found for ID: " + roomId));

            // Redis에서 메시지 가져오기
            List<Map<String, String>> redisMessages = redisService.getChatMessages(roomId);

            // Redis 메시지를 ChatMessage 엔티티로 변환
            List<ChatMessage> messages = redisMessages.stream()
                    .map(parsedMap -> ChatMessage.fromMap(parsedMap, chatRoom))
                    .collect(Collectors.toList());

            // MySQL에 저장
            chatMessageRepository.saveAll(messages);

            // Redis에서 키 삭제
            redisService.deleteKey(roomKey);
        }
    }
}
