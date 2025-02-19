package store.storymate.storymatebackend.reading.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.reading.api.dto.request.BookmarkCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.HighlightCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteUpdateRequest;
import store.storymate.storymatebackend.reading.api.dto.response.BookmarkResponse;
import store.storymate.storymatebackend.reading.api.dto.response.HighlightResponse;
import store.storymate.storymatebackend.reading.api.dto.response.NoteResponse;

@Tag(name = "[작품 감상 API]", description = "작품 감상 관련 API")
public interface MemberBookDocs {

    @Operation(
            summary = "읽은 책으로 표시",
            description = "책 ID에 해당하는 책을 읽은 책 목록에 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "책으로 표시 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> markAsRead(@Parameter(description = "책 ID") @PathVariable Long bookId);

    @Operation(
            summary = "하이라이트 입력",
            description = "책 ID에 해당하는 책에 하이라이트를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "하이라이트 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> createHighlight(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                              @RequestBody HighlightCreateRequest highlightCreateRequest);

    @Operation(
            summary = "북마크 입력",
            description = "책 ID에 해당하는 책에 북마크를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "북마크 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> createBookmark(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                             @RequestBody BookmarkCreateRequest bookmarkCreateRequest);

    @Operation(
            summary = "노트 입력",
            description = "책 ID에 해당하는 책에 메모를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "메모 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> createNote(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                         @RequestBody NoteCreateRequest noteCreateRequest);

    @Operation(
            summary = "메모 수정",
            description = "책 ID에 해당하는 책의 메모를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메모 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> updateNote(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                         @RequestBody NoteUpdateRequest noteUpdateRequest);

    @Operation(
            summary = "하이라이트 조회",
            description = "책 ID에 해당하는 책의 하이라이트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하이라이트 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<List<HighlightResponse>> getHighlights(
            @Parameter(description = "책 ID") @PathVariable Long bookId);

    @Operation(
            summary = "북마크 조회",
            description = "책 ID에 해당하는 책의 북마크를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "북마크 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<List<BookmarkResponse>> getBookmarks(
            @Parameter(description = "책 ID") @PathVariable Long bookId);

    @Operation(
            summary = "메모 조회",
            description = "책 ID에 해당하는 책의 메모를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메모 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<List<NoteResponse>> getNotes(@Parameter(description = "책 ID") @PathVariable Long bookId);

    @Operation(
            summary = "하이라이트 삭제",
            description = "책 ID에 해당하는 책의 하이라이트를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "하이라이트 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> deleteHighlight(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                              @Parameter(description = "하이라이트 ID") @PathVariable Long highlightId);

    @Operation(
            summary = "북마크 삭제",
            description = "책 ID에 해당하는 책의 북마크를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "북마크 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> deleteBookmark(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                             @Parameter(description = "북마크 ID") @PathVariable Long bookmarkId);

    @Operation(
            summary = "메모 삭제",
            description = "책 ID에 해당하는 책의 메모를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메모 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    ApiResponseTemplate<Void> deleteNote(@Parameter(description = "책 ID") @PathVariable Long bookId,
                                         @Parameter(description = "메모 ID") @PathVariable Long noteId);
}