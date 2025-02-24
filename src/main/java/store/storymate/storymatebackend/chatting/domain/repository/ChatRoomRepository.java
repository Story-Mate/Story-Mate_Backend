package store.storymate.storymatebackend.chatting.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomCustomRepository {

    @Query("SELECT c FROM ChatRoom c JOIN FETCH c.member WHERE c.id = :roomId")
    Optional<ChatRoom> findByIdWithMember(@Param("roomId") Long roomId);
}