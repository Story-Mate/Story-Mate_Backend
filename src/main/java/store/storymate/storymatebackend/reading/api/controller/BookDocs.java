package store.storymate.storymatebackend.reading.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.reading.api.dto.response.BookDetailResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookRecommendResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;

public interface BookDocs {
    @Operation(
            summary = "책 목록 조회",
            description = "장르 및 페이징 정보를 바탕으로 책 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "책 목록 조회 성공",
                            content = @Content(schema = @Schema(implementation = BookResponseList.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<BookResponseList> getBooks(
            @Parameter(description = "검색어") @RequestParam(value = "query", required = false) String query,
            @Parameter(description = "검색 타입 (TITLE, AUTHOR, KEYWORD, TAG)") @RequestParam(value = "searchType", required = false) String searchType,
            @Parameter(description = "필터링할 장르 (FAIRY_TALE, SHORT_STORY, NOVEL)") @RequestParam(value = "genre", required = false) String genre,
            @Parameter(description = "정렬 기준 (LATEST, RECOMMENDED, POPULAR)") @RequestParam(value = "sortType", required = false) String sortType,
            Pageable pageable);

    @Operation(
            summary = "책 상세 조회",
            description = "책 ID에 해당하는 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "책 상세 조회 성공",
                            content = @Content(schema = @Schema(implementation = BookDetailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<BookDetailResponse> getBookDetails(
            @Parameter(description = "조회할 책의 ID") @RequestParam(value = "bookId") Long bookId);

    @Operation(
            summary = "책 추천",
            description = "JWT에서 유저ID를 조회해 추천하는 책을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "책 추천 조회 성공",
                            content = @Content(schema = @Schema(implementation = BookRecommendResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<BookRecommendResponse> getBookRecommend(
            // @AuthenticationPrincipal UserDetails userDetails
    );
}
