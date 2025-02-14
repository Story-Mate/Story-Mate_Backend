package store.storymate.storymatebackend.reading.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.reading.api.dto.request.BookmarkCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.HighlightCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteUpdateRequest;
import store.storymate.storymatebackend.reading.api.dto.response.BookmarkResponse;
import store.storymate.storymatebackend.reading.api.dto.response.HighlightResponse;
import store.storymate.storymatebackend.reading.api.dto.response.NoteResponse;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.Bookmark;
import store.storymate.storymatebackend.reading.domain.Highlight;
import store.storymate.storymatebackend.reading.domain.MemberBook;
import store.storymate.storymatebackend.reading.domain.Note;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;
import store.storymate.storymatebackend.reading.domain.repository.BookmarkRepository;
import store.storymate.storymatebackend.reading.domain.repository.HighlightRepository;
import store.storymate.storymatebackend.reading.domain.repository.MemberBookRepository;
import store.storymate.storymatebackend.reading.domain.repository.NoteRepository;
import store.storymate.storymatebackend.reading.exception.BookNotFoundException;
import store.storymate.storymatebackend.reading.exception.BookmarkNotFoundException;
import store.storymate.storymatebackend.reading.exception.HighlightNotFoundException;
import store.storymate.storymatebackend.reading.exception.MemberBookNotFoundException;
import store.storymate.storymatebackend.reading.exception.NoteNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberBookServiceImpl implements MemberBookService {

    private final BookRepository bookRepository;
    private final MemberBookRepository memberBookRepository;
    private final NoteRepository noteRepository;
    private final BookmarkRepository bookmarkRepository;
    private final HighlightRepository highlightRepository;

    private final MemberUtil memberUtil;

    @Override
    public void createMemberBook(Long bookId) {
        Member member = memberUtil.getCurrentMember();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        MemberBook memberBook = MemberBook.builder()
                .member(member)
                .book(book)
                .build();
        memberBookRepository.save(memberBook);
    }

    @Override
    public void createBookmark(Long bookId, BookmarkCreateRequest bookmarkCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Bookmark bookmark = BookmarkCreateRequest.toEntity(memberBook, bookmarkCreateRequest);
        bookmarkRepository.save(bookmark);
    }

    @Override
    public List<BookmarkResponse> getBookmarks(Long bookId) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        return bookmarkRepository.findByMemberBook(memberBook).stream()
                .map(BookmarkResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(BookmarkNotFoundException::new);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public void createNote(Long bookId, NoteCreateRequest noteCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Note note = NoteCreateRequest.toEntity(memberBook, noteCreateRequest);
        noteRepository.save(note);
    }

    @Override
    public List<NoteResponse> getNotes(Long bookId) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        return noteRepository.findByMemberBook(memberBook).stream()
                .map(NoteResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateNote(NoteUpdateRequest noteUpdateRequest) {
        Note note = noteRepository.findById(noteUpdateRequest.noteId())
                .orElseThrow(NoteNotFoundException::new);
        note.updateContent(noteUpdateRequest.content());
        noteRepository.save(note);
    }

    @Override
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(NoteNotFoundException::new);
        noteRepository.delete(note);
    }

    @Override
    public void createHighlight(Long bookId, HighlightCreateRequest highlightCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Highlight highlight = HighlightCreateRequest.toEntity(memberBook, highlightCreateRequest);
        highlightRepository.save(highlight);
    }

    @Override
    public List<HighlightResponse> getHighlights(Long bookId) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        return highlightRepository.findByMemberBook(memberBook).stream()
                .map(HighlightResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHighlight(Long highlightId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(HighlightNotFoundException::new);
        highlightRepository.delete(highlight);
    }
}
