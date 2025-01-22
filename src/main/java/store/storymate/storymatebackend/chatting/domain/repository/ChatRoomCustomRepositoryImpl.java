package store.storymate.storymatebackend.chatting.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoom> findChatRoomsByMember(Member member, Pageable pageable) {
        QChatRoom chatRoom = QChatRoom.chatRoom;

        // QueryDSL 쿼리 작성
        List<ChatRoom> chatRooms = queryFactory.selectFrom(chatRoom)
                .where(
                        isFromMember(member).or(isToMember(member))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory.selectFrom(chatRoom)
                .where(
                        isFromMember(member).or(isToMember(member))
                )
                .fetchCount();

        return new PageImpl<>(chatRooms, pageable, totalCount);
    }

    // 조건 메서드
    private BooleanExpression isFromMember(Member member) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return chatRoom.fromMember.eq(member);
    }

    private BooleanExpression isToMember(Member member) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return chatRoom.toMember.eq(member);
    }
}