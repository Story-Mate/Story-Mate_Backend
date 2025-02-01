package store.storymate.storymatebackend.chatting.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.member.domain.Member;

public interface ChatRoomCustomRepository {
    Page<ChatRoom> findChatRoomsByMember(Member member, Pageable pageable);
}
