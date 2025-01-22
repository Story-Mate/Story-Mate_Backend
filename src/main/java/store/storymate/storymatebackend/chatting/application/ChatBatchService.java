package store.storymate.storymatebackend.chatting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatBatchService {

    private final ChatMessageService chatMessageService;

    @Scheduled(fixedRate = 600000)
    public void persistMessages() {
        System.out.println("Scheduled task running...");
        chatMessageService.persistMessagesToDB();
    }
}
