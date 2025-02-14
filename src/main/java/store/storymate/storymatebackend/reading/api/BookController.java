package store.storymate.storymatebackend.reading.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.reading.api.dto.response.BookDetailResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookRecommendResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;
import store.storymate.storymatebackend.reading.application.BookService;
import store.storymate.storymatebackend.reading.application.RecommendationService;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController implements BookDocs {

    private final BookService bookService;
    private final RecommendationService recommendationService;

    @Override
    @GetMapping
    public ApiResponseTemplate<BookResponseList> getBooks(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "sortType", required = false) String sortType, Pageable pageable) {

        BookResponseList books = bookService.getBooks(query, searchType, genre, sortType, pageable);
        return ApiResponseTemplate.ok("책 목록 조회 성공", books);
    }

    @Override
    @GetMapping("/{bookId}")
    public ApiResponseTemplate<BookDetailResponse> getBookDetails(@PathVariable("bookId") Long bookId) {
        BookDetailResponse bookDetails = bookService.getBookDetails(bookId);
        return ApiResponseTemplate.ok("책 상세 조회 성공", bookDetails);
    }

    @Override
    @GetMapping("/recommend")
    public ApiResponseTemplate<BookRecommendResponse> getBookRecommend(
//            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // TODO 사용자 ID를 받아와야 함
        BookRecommendResponse recommendedBook = recommendationService.recommendBookForUser(1L);
        return ApiResponseTemplate.ok("책 추천 조회 성공", recommendedBook);
    }
}
