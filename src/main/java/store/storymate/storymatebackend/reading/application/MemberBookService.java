package store.storymate.storymatebackend.reading.application;

import java.util.List;
import store.storymate.storymatebackend.reading.api.dto.request.BookmarkCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.HighlightCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteUpdateRequest;
import store.storymate.storymatebackend.reading.api.dto.response.BookmarkResponse;
import store.storymate.storymatebackend.reading.api.dto.response.HighlightResponse;
import store.storymate.storymatebackend.reading.api.dto.response.NoteResponse;

public interface MemberBookService {

    /**
     * 읽은 책 목록에 추가: 사용자가 읽은 책 목록에 책을 추가합니다.
     *
     * @param bookId 책 ID
     * @return 읽은 책 목록 추가 결과
     */
    void createMemberBook(Long bookId);

    /**
     * 북마크 입력
     *
     * @param bookId 책 ID
     * @param bookmarkCreateRequest 북마크 정보
     * @return 북마크 결과
     */
    void createBookmark(Long bookId, BookmarkCreateRequest bookmarkCreateRequest);

    /**
     * 북마크 목록 조회
     *
     * @param bookId 책 ID
     */
    List<BookmarkResponse> getBookmarks(Long bookId);

    /**
     * 북마크 삭제
     *
     * @param bookmarkId 삭제할 북마크 ID
     * @return 북마크 삭제 결과
     */
    void deleteBookmark(Long bookmarkId);

    /**
     * 노트 입력
     *
     * @param noteCreateRequest 노트 정보
     * @return 노트 결과
     */
    void createNote(Long bookId, NoteCreateRequest noteCreateRequest);

    /**
     * 노트 목록 조회
     *
     * @param bookId 책 ID
     */
    List<NoteResponse> getNotes(Long bookId);

    /**
     * 노트 수정
     *
     * @param noteUpdateRequest 수정할 노트 정보
     * @return 노트 수정 결과
     */
    void updateNote(NoteUpdateRequest noteUpdateRequest);

    /**
     * 노트 삭제
     *
     * @param noteId 삭제할 노트 ID
     * @return 노트 삭제 결과
     */
    void deleteNote(Long noteId);

    /**
     * 하이라이트 입력
     *
     * @param highlightCreateRequest 하이라이트 정보
     * @return 하이라이트 결과
     */
    void createHighlight(Long bookId, HighlightCreateRequest highlightCreateRequest);

    /**
     * 하이라이트 목록 조회
     *
     * @param bookId 책 ID
     */
    List<HighlightResponse> getHighlights(Long bookId);

    /**
     * 하이라이트 삭제
     *
     * @param highlightId 삭제할 하이라이트 ID
     * @return 하이라이트 삭제 결과
     */
    void deleteHighlight(Long highlightId);
}
