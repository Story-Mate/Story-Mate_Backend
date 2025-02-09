package store.storymate.storymatebackend.reading.application;

import store.storymate.storymatebackend.reading.api.dto.response.BookRecommendResponse;

public interface RecommendationService {
    /**
     * 사용자 감상 이력 기반 추천
     *
     * @param userId 추천을 요청한 사용자 ID
     * @return 추천 책(Optional 처리)
     */
    BookRecommendResponse recommendBookForUser(Long userId);
}
