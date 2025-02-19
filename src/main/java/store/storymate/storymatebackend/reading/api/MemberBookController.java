package store.storymate.storymatebackend.reading.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.reading.api.dto.request.BookmarkCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.HighlightCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteUpdateRequest;
import store.storymate.storymatebackend.reading.api.dto.response.BookmarkResponse;
import store.storymate.storymatebackend.reading.api.dto.response.HighlightResponse;
import store.storymate.storymatebackend.reading.api.dto.response.NoteResponse;
import store.storymate.storymatebackend.reading.application.MemberBookService;

@RestController
@RequestMapping("/api/books/{bookId}")
@RequiredArgsConstructor
public class MemberBookController implements MemberBookDocs {

    private final MemberBookService memberBookService;

    @PutMapping("/contents")
    public ApiResponseTemplate<Void> markAsRead(@PathVariable Long bookId) {
        memberBookService.createMemberBook(bookId);
        return ApiResponseTemplate.ok("책을 읽은 목록에 추가 성공");
    }

    @PostMapping("/highlights")
    public ApiResponseTemplate<Void> createHighlight(@PathVariable Long bookId,
                                                     @RequestBody HighlightCreateRequest highlightCreateRequest) {
        memberBookService.createHighlight(bookId, highlightCreateRequest);
        return ApiResponseTemplate.ok("하이라이트 추가 성공");
    }

    @PostMapping("/bookmarks")
    public ApiResponseTemplate<Void> createBookmark(@PathVariable Long bookId,
                                                    @RequestBody BookmarkCreateRequest bookmarkCreateRequest) {
        memberBookService.createBookmark(bookId, bookmarkCreateRequest);
        return ApiResponseTemplate.ok("북마크 추가 성공");
    }

    @PostMapping("/notes")
    public ApiResponseTemplate<Void> createNote(@PathVariable Long bookId,
                                                @RequestBody NoteCreateRequest noteCreateRequest) {
        memberBookService.createNote(bookId, noteCreateRequest);
        return ApiResponseTemplate.ok("메모 추가 성공");
    }

    @PatchMapping("/notes")
    public ApiResponseTemplate<Void> updateNote(@PathVariable Long bookId,
                                                @RequestBody NoteUpdateRequest noteUpdateRequest) {
        memberBookService.updateNote(noteUpdateRequest);
        return ApiResponseTemplate.ok("메모 수정 성공");
    }

    @GetMapping("/highlights")
    public ApiResponseTemplate<List<HighlightResponse>> getHighlights(@PathVariable Long bookId) {
        List<HighlightResponse> highlights = memberBookService.getHighlights(bookId);
        return ApiResponseTemplate.ok("하이라이트 조회 성공", highlights);
    }

    @GetMapping("/bookmarks")
    public ApiResponseTemplate<List<BookmarkResponse>> getBookmarks(@PathVariable Long bookId) {
        List<BookmarkResponse> bookmarks = memberBookService.getBookmarks(bookId);
        return ApiResponseTemplate.ok("북마크 조회 성공", bookmarks);
    }

    @GetMapping("/notes")
    public ApiResponseTemplate<List<NoteResponse>> getNotes(@PathVariable Long bookId) {
        List<NoteResponse> notes = memberBookService.getNotes(bookId);
        return ApiResponseTemplate.ok("메모 조회 성공", notes);
    }

    @DeleteMapping("/highlights/{highlightId}")
    public ApiResponseTemplate<Void> deleteHighlight(@PathVariable Long bookId, @PathVariable Long highlightId) {
        memberBookService.deleteHighlight(highlightId);
        return ApiResponseTemplate.ok("하이라이트 삭제 성공");
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public ApiResponseTemplate<Void> deleteBookmark(@PathVariable Long bookId, @PathVariable Long bookmarkId) {
        memberBookService.deleteBookmark(bookmarkId);
        return ApiResponseTemplate.ok("북마크 삭제 성공");
    }

    @DeleteMapping("/notes/{noteId}")
    public ApiResponseTemplate<Void> deleteNote(@PathVariable Long bookId, @PathVariable Long noteId) {
        memberBookService.deleteNote(noteId);
        return ApiResponseTemplate.ok("메모 삭제 성공");
    }
}
