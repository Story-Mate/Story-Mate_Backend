package store.storymate.storymatebackend.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.application.ChatMessageService;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("{roomId}")
    public ApiResponseTemplate<ChatMessageResList> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ChatMessageResList chatMessageResList = chatMessageService.findChatMessages(roomId, PageRequest.of(page, size));

        return ApiResponseTemplate.ok("내 채팅방 조회", chatMessageResList);

    }
}