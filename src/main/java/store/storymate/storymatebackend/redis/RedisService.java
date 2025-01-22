package store.storymate.storymatebackend.redis;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 채팅 메시지를 Redis List에 저장
     */
    public void saveChatMessage(String roomId, String message, Duration duration) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        String redisKey = "chat:room:" + roomId;

        // 메시지 저장
        listOps.rightPush(redisKey, message);

        // TTL 설정
        redisTemplate.expire(redisKey, duration);
    }

    /**
     * Redis List에서 모든 메시지 조회
     */
    public List<Map<String, String>> getChatMessages(String roomId) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        String redisKey = "chat:room:" + roomId;

        try {
            // Redis에서 메시지 가져오기
            List<Object> messages = listOps.range(redisKey, 0, -1);

            // 메시지를 Map 형식으로 변환

            return messages.stream()
                    .map(message -> parseToMap(message.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();

            return List.of();
        }
    }

    private Map<String, String> parseToMap(String message) {
        Map<String, String> map = new HashMap<>();
        String[] entries = message.replace("{", "").replace("}", "").split(", ");

        for (String entry : entries) {
            String[] keyValue = entry.split("=", 2);
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return map;
    }

    /**
     * Redis에서 특정 키 삭제
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Redis에서 패턴에 매칭되는 모든 키 조회
     */
    public List<String> getKeys(String pattern) {
        List<String> keys = new ArrayList<>();
        Cursor<byte[]> cursor = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .scan(ScanOptions.scanOptions().match(pattern).count(1000).build());

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
        }
        return keys;
    }

}
