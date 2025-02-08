package store.storymate.storymatebackend.chatting.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
}