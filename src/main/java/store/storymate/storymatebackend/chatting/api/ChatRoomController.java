package store.storymate.storymatebackend.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatRoomReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatRoomResList;
import store.storymate.storymatebackend.chatting.application.ChatRoomService;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatRoomController implements ChatRoomDocs{

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ApiResponseTemplate<ChatRoomResDto> createChatRoom(
            @RequestBody ChatRoomReqDto request) {
        ChatRoomResDto chatRoomResDto = chatRoomService.createChatRoom(request);

        return ApiResponseTemplate.created("채팅방 생성", chatRoomResDto);
    }

    @GetMapping
    public ApiResponseTemplate<ChatRoomResList> getChatRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ChatRoomResList chatRooms = chatRoomService.getChatRooms(PageRequest.of(page, size));

        return ApiResponseTemplate.ok("내 채팅방 조회", chatRooms);
    }
}