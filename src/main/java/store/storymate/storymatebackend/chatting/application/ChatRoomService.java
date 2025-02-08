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
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberUtil memberUtil;
    private final CharactersRepository charactersRepository;

    @Transactional
    public ChatRoomResDto createChatRoom(ChatRoomReqDto chatRoomReqDto) {
        Member member = memberUtil.getCurrentMember();

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
                member.getOauthInfo().getNickname(),
                member.getProfileImageUrl(),
                characters.getName(),
                characters.getImageUrl());
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
}
