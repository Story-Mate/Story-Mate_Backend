package store.storymate.storymatebackend.chatting.application;

import java.util.List;
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
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;
import store.storymate.storymatebackend.member.exception.MemberNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final CharactersRepository charactersRepository;

    @Transactional
    public ChatRoomResDto createChatRoom(String email, ChatRoomReqDto chatRoomReqDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Characters characters = charactersRepository.findById(chatRoomReqDto.charactersId())
                .orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder()
                .status(Status.ACTIVE)
                .title(chatRoomReqDto.title())
                .liking(0)
                .member(member)
                .characters(characters)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResDto.from(
                savedChatRoom.getId(),
                savedChatRoom.getTitle(),
                savedChatRoom.getLiking(),
                member.getName(),
                member.getProfileImageUrl(),
                characters.getName(),
                characters.getImageUrl());
    }

    @Transactional
    // TODO: 추후에 작품명도 추가로 제공해줘야 함.
    public ChatRoomResList getChatRooms(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Page<ChatRoom> chatRoomPage = chatRoomRepository.findChatRoomsByMember(member, pageable);

        List<ChatRoomResDto> chatRoomResDtos = chatRoomPage.getContent().stream()
                .map(chatRoom -> ChatRoomResDto.builder()
                        .roomId(chatRoom.getId())
                        .title(chatRoom.getTitle())
                        .liking(chatRoom.getLiking())
                        .loginUserName(member.getName())
                        .memberImage(member.getProfileImageUrl())
                        .charactersName(chatRoom.getCharacters().getName())
                        .charactersImage(chatRoom.getCharacters().getImageUrl())
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
}