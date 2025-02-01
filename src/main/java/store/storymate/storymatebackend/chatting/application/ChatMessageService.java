package store.storymate.storymatebackend.chatting.application;

import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

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

        chatMessageRepository.save(chatMessage);
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
