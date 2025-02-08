package store.storymate.storymatebackend.chatting.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatMessageSaveReqDto;
import store.storymate.storymatebackend.chatting.application.ChatMessageService;
import store.storymate.storymatebackend.chatting.application.ChatRoomService;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] data = payload.split(":", 3); // ":" 기준으로 메시지 분리 (sender:roomId:content)

        if (data.length < 3) {
            session.sendMessage(new TextMessage("Invalid message format. Use 'sender:roomId:message' format."));
            return;
        }

        String sender = data[0]; // 발신자
        String roomId = data[1]; // 채팅방 ID
        String content = data[2]; // 메시지 내용

        String charactersName = chatRoomService.getCharacterName(Long.parseLong(roomId));

        ChatMessageSaveReqDto chatMessageSaveMemberReqDto = 
                ChatMessageSaveReqDto.builder()
                        .roomId(roomId)
                        .sender(sender)
                        .content(content)
                        .build();

        // 1. 메시지 저장
        chatMessageService.saveMessage(chatMessageSaveMemberReqDto);

        ChatMessageSaveReqDto chatMessageAi =
                ChatMessageSaveReqDto.builder()
                        .roomId(roomId)
                        .sender(charactersName)
                        .content(content)
                        .build();

        // 2. AI 서버에 메시지 보내고 응답 받기
        String aiResponse = chatMessageService.callAiApi(chatMessageAi);

        ChatMessageSaveReqDto chatMessageSaveAiReqDto =
                ChatMessageSaveReqDto.builder()
                        .roomId(roomId)
                        .sender(charactersName)
                        .content(aiResponse)
                        .build();

        // 3. AI 응답 저장
        chatMessageService.saveMessage(chatMessageSaveAiReqDto);

        // 4. 사용자 메시지와 AI 응답을 모두 채팅방에 전송
        broadcastMessageToRoom(roomId, sender + ": " + content);
        broadcastMessageToRoom(roomId, charactersName + ": " + aiResponse);
    }

    // 채팅방의 모든 사용자에게 메시지 전송
    private void broadcastMessageToRoom(String roomId, String message) throws Exception {
        if (chatRooms.containsKey(roomId)) {
            for (WebSocketSession webSocketSession : chatRooms.get(roomId)) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(message));
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
}
