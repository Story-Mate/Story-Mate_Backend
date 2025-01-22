package store.storymate.storymatebackend.chatting.config;

import com.example.mixmix.chatting.application.ChatMessageService;
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

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();
    private final ChatMessageService chatMessageService;

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
        String chatMessage = data[2]; // 메시지 내용

        // 메시지 저장
        chatMessageService.saveMessage(roomId, sender, chatMessage);

        // 해당 채팅방에 메시지 전송
        if (chatRooms.containsKey(roomId)) {
            for (WebSocketSession webSocketSession : chatRooms.get(roomId)) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(sender + ": " + chatMessage));
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
