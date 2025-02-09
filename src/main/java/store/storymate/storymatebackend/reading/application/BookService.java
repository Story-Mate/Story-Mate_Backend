package store.storymate.storymatebackend.reading.application;

import org.springframework.data.domain.Pageable;
import store.storymate.storymatebackend.reading.api.dto.response.BookDetailResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;

public interface BookService {
    /**
     * 책 목록 조회: 기준에 따라 책 목록을 조회 후, BookResponse 리스트로 반환합니다.
     *
     * @param query      검색어
     * @param searchType 검색 타입 (TITLE, AUTHOR, KEYWORD, TAG) 없으면 전체 검색
     * @param genre    조회할 책의 장르 (장르가 없을 경우 전체 책 목록을 조회합니다.)
     * @param sortType 조회할 책의 정렬 기준 (최신순, 인기순, 추천순)
     * @param pageable 페이징 정보
     * @return 책 목록 조회 결과
     */
    BookResponseList getBooks(String query, String searchType, String genre, String sortType, Pageable pageable);

    /**
     * 책 상세 조회: 책 ID에 해당하는 상세 정보를 조회하여 BookDetailDto로 반환합니다.
     *
     * @param bookId 조회할 책의 ID
     * @return 책 상세 정보(Optional 처리)
     */
    BookDetailResponse getBookDetails(Long bookId);
}
