package store.storymate.storymatebackend.chatting.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatRoomReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResList;
import store.storymate.storymatebackend.chatting.domain.ChatMessage;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.chatting.domain.repository.ChatMessageRepository;
import store.storymate.storymatebackend.chatting.domain.repository.ChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
//    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResDto createChatRoom(String email, ChatRoomReqDto chatRoomReqDto) {

        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomReqDto.roomName())
                .fromMember(frommember)
                .toMember(toMember)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResDto.from(savedChatRoom.getId(),
                savedChatRoom.getRoomName(),
                frommember.getId(),
                toMember.getId(),
                frommember.getName(),
                null,
                0,
                toMember.getPicture());
    }

    public ChatRoomResList getChatRooms(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Page<ChatRoom> chatRoomPage = chatRoomRepository.findChatRoomsByMember(member, pageable);

        List<ChatRoomResDto> chatRoomResDtos = chatRoomPage.getContent().stream()
                .map(chatRoom -> ChatRoomResDto.builder()
                        .roomId(chatRoom.getId())
                        .name(chatRoom.getRoomName())
                        .fromMemberId(chatRoom.getFromMember().getId())
                        .toMemberId(chatRoom.getToMember().getId())
                        .loginUserName(member.getName())
                        .recentMessage(getRecentMessage(chatRoom))
                        .unreadNotification(notificationRepository.countUnreadChatNotifications(member, chatRoom))
                        .memberImage(getOtherMemberImage(chatRoom, member))
                        .build()
                )
                .toList();

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .totalPages(chatRoomPage.getTotalPages())
                .totalItems(chatRoomPage.getTotalElements())
                .currentPage(chatRoomPage.getNumber())
                .build();

        return ChatRoomResList.of(chatRoomResDtos, pageInfoResDto);
    }

    private String getOtherMemberImage(ChatRoom chatRoom, Member currentUser) {
        if (chatRoom.getFromMember().getId().equals(currentUser.getId())) {
            return chatRoom.getToMember().getPicture();
        } else {
            return chatRoom.getFromMember().getPicture();
        }
    }

    private String getRecentMessage(ChatRoom chatRoom) {
        Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoomOrderByTimestampDesc(chatRoom);
        if (chatMessage.isPresent()) {
            return chatMessage.get().getContent();
        }
        return null;
    }
}