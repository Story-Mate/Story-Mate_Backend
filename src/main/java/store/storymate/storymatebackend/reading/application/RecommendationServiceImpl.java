package store.storymate.storymatebackend.reading.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.reading.api.dto.response.BookRecommendResponse;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;

@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final BookRepository bookRepository;

    @Override
    public BookRecommendResponse recommendBookForUser(Long userId) {
        return bookRepository.getRecommendedBookForUser(userId)
                .map(BookRecommendResponse::fromEntity)
                .orElse(null);
    }
}
