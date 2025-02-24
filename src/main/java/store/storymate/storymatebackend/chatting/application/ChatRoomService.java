package store.storymate.storymatebackend.chatting.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.characters.domain.Characters;
import store.storymate.storymatebackend.characters.domain.repository.CharactersRepository;
import store.storymate.storymatebackend.chatting.api.dto.PageInfoResDto;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatRoomReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResList;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.chatting.domain.repository.ChatRoomRepository;
import store.storymate.storymatebackend.chatting.exception.ExistsChatRoomException;
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberUtil memberUtil;
    private final CharactersRepository charactersRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ChatRoomResDto createChatRoom(ChatRoomReqDto chatRoomReqDto) {
        Member member = memberUtil.getCurrentMember();

        Characters characters = charactersRepository.findById(chatRoomReqDto.charactersId())
                .orElseThrow();

        Book book = bookRepository.findBookByTitle(chatRoomReqDto.bookTitle())
                .orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder()
                .status(Status.ACTIVE)
                .title(chatRoomReqDto.title())
                .liking(0)
                .member(member)
                .characters(characters)
                .book(book)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResDto.from(
                savedChatRoom.getId(),
                savedChatRoom.getTitle(),
                savedChatRoom.getLiking(),
                member.getOauthInfo().getNickname(),
                member.getProfileImageUrl(),
                characters.getName(),
                characters.getImageUrl(),
                book.getTitle());
    }

    public ChatRoomResList getChatRooms(Pageable pageable) {
        Member member = memberUtil.getCurrentMember();

        Page<ChatRoom> chatRoomPage = chatRoomRepository.findChatRoomsByMember(member, pageable);

        List<ChatRoomResDto> chatRoomResDtos = chatRoomPage.getContent().stream()
                .map(chatRoom -> ChatRoomResDto.builder()
                        .roomId(chatRoom.getId())
                        .title(chatRoom.getTitle())
                        .liking(chatRoom.getLiking())
                        .loginUserName(member.getOauthInfo().getNickname())
                        .memberImage(member.getProfileImageUrl())
                        .charactersName(chatRoom.getCharacters().getName())
                        .charactersImage(chatRoom.getCharacters().getImageUrl())
                        .bookTitle(chatRoom.getBook().getTitle())
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

    // 웹소켓 연결 시 캐릭터 이름을 알아내기 위한 메서드 (service 층에서만 접근 가능)
    public String getCharacterName(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ExistsChatRoomException::new);

        return chatRoom.getCharacters().getName();
    }

    public Optional<ChatRoom> findChatRoomById(Long roomId) {
        return Optional.ofNullable(chatRoomRepository.findByIdWithMember(roomId)
                .orElseThrow(ExistsChatRoomException::new));
    }
}
