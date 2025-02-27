package store.storymate.storymatebackend.chatting.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatMessageSaveReqDto;
import store.storymate.storymatebackend.chatting.application.ChatMessageService;
import store.storymate.storymatebackend.chatting.application.ChatRoomService;
import store.storymate.storymatebackend.chatting.domain.ChatRoom;
import store.storymate.storymatebackend.member.application.MemberService;
import store.storymate.storymatebackend.member.domain.Member;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ParsedMessage parsedMessage = parseMessage(message.getPayload());
        if (parsedMessage == null) {
            session.sendMessage(
                    new TextMessage("Invalid message format. Use 'sender:roomId:bookName:message' format."));
            return;
        }

        // 메시지 카운트 검증 및 감소
        if (!validateAndDecreaseMessageCount(parsedMessage.roomId(), session)) {
            return;
        }

        // 사용자 메시지 저장
        saveUserMessage(parsedMessage);

        // AI 응답 처리 및 저장
        processAiResponse(parsedMessage);
    }

    private ParsedMessage parseMessage(String payload) {
        String[] data = payload.split(":", 4);
        if (data.length < 4) {
            return null;
        }
        return new ParsedMessage(data[0], data[1], data[2], data[3]);
    }

    private boolean validateAndDecreaseMessageCount(String roomId, WebSocketSession session) throws Exception {
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findChatRoomById(Long.parseLong(roomId));
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            Member member = chatRoom.getMember();

            if (member != null && member.getMessageCount() <= 0) {
                session.sendMessage(new TextMessage("⚠️ 메시지를 보낼 수 없습니다. 남은 메시지 횟수가 없습니다."));
                return false;
            }
            memberService.decreaseMessageCount(member.getId());
        }
        return true;
    }

    private void saveUserMessage(ParsedMessage parsedMessage) throws Exception {
        ChatMessageSaveReqDto chatMessageDto = ChatMessageSaveReqDto.builder()
                .roomId(parsedMessage.roomId())
                .sender(parsedMessage.sender())
                .content(parsedMessage.content())
                .build();

        chatMessageService.saveMessage(chatMessageDto);

        broadcastMessage(parsedMessage.roomId(), parsedMessage.sender(), parsedMessage.content());
    }

    private void processAiResponse(ParsedMessage parsedMessage) throws Exception {
        String charactersName = chatRoomService.getCharacterName(Long.parseLong(parsedMessage.roomId()));

        ChatMessageSaveReqDto chatMessageAi = ChatMessageSaveReqDto.builder()
                .roomId(parsedMessage.roomId())
                .sender(charactersName)
                .content(parsedMessage.content())
                .bookTitle(parsedMessage.bookTitle())
                .build();

        String aiResponse = chatMessageService.callAiApi(chatMessageAi);

        ChatMessageSaveReqDto chatMessageSaveAiReqDto = ChatMessageSaveReqDto.builder()
                .roomId(parsedMessage.roomId())
                .sender(charactersName)
                .content(aiResponse)
                .build();

        chatMessageService.saveMessage(chatMessageSaveAiReqDto);
        broadcastMessage(parsedMessage.roomId(), charactersName, aiResponse);
    }

    private void broadcastMessage(String roomId, String sender, String content) throws Exception {
        if (chatRooms.containsKey(roomId)) {
            for (WebSocketSession webSocketSession : chatRooms.get(roomId)) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(sender + ": " + content));
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String uri = session.getUri().toString();
        String roomId = uri.substring(uri.lastIndexOf("/") + 1);
        chatRooms.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatRooms.forEach((roomId, sessions) -> sessions.remove(session));
    }

    private record ParsedMessage(String sender, String roomId, String bookTitle, String content) {
    }
}
