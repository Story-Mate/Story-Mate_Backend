package store.storymate.storymatebackend.reading.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.reading.api.dto.PageInfoDto;
import store.storymate.storymatebackend.reading.api.dto.request.BookmarkCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.HighlightCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.MemberBookProgressRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteCreateRequest;
import store.storymate.storymatebackend.reading.api.dto.request.NoteUpdateRequest;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;
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

    @Value("${reading.completion.threshold}")
    private int completionThreshold;

    @Override
    @Transactional
    public void createMemberBook(Long bookId, MemberBookProgressRequest request) {
        Member member = memberUtil.getCurrentMember();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        MemberBook memberBook = MemberBook.builder()
                .member(member)
                .book(book)
                .progress(request.progress())
                .build();

        memberBookRepository.findByMemberIdAndBookId(member.getId(), bookId)
                .ifPresentOrElse(
                        mb -> {
                            if (mb.getProgress() < request.progress()) {
                                mb.updateProgress(request.progress());
                                checkAndRewardForCompletedBook(mb, member);
                            }
                        },
                        () -> {
                            memberBook.updateProgress(request.progress());
                            memberBookRepository.save(memberBook);
                            checkAndRewardForCompletedBook(memberBook, member);
                        }
                );
    }

    private static void checkAndRewardForCompletedBook(MemberBook memberBook, Member member) {
        if (memberBook.getProgress() == 100f) {
            member.addMessageCount(10L);
        }
    }

    @Override
    @Transactional
    public void createBookmark(Long bookId, BookmarkCreateRequest bookmarkCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Bookmark bookmark = BookmarkCreateRequest.toEntity(memberBook, bookmarkCreateRequest);
        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(BookmarkNotFoundException::new);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    @Transactional
    public void createNote(Long bookId, NoteCreateRequest noteCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Note note = NoteCreateRequest.toEntity(memberBook, noteCreateRequest);
        noteRepository.save(note);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void updateNote(NoteUpdateRequest noteUpdateRequest) {
        Note note = noteRepository.findById(noteUpdateRequest.noteId())
                .orElseThrow(NoteNotFoundException::new);
        note.updateContent(noteUpdateRequest.content());
        noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(NoteNotFoundException::new);
        noteRepository.delete(note);
    }

    @Override
    @Transactional
    public void createHighlight(Long bookId, HighlightCreateRequest highlightCreateRequest) {
        MemberBook memberBook = memberBookRepository.findByMemberIdAndBookId(
                memberUtil.getCurrentMember().getId(),
                bookId
        ).orElseThrow(MemberBookNotFoundException::new);
        Highlight highlight = HighlightCreateRequest.toEntity(memberBook, highlightCreateRequest);
        highlightRepository.save(highlight);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void deleteHighlight(Long highlightId) {
        Highlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(HighlightNotFoundException::new);
        highlightRepository.delete(highlight);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseList getReadingBooks(Pageable pageable) {
        Page<MemberBook> memberBooksPage = memberBookRepository.findByMemberIdAndProgressIsLessThan(
                memberUtil.getCurrentMember().getId(), (float) completionThreshold, pageable);

        List<BookResponse> readingBooks = memberBooksPage.stream()
                .map(MemberBook::getBook)
                .map(BookResponse::fromEntity)
                .toList();

        return BookResponseList.of(readingBooks, PageInfoDto.from(memberBooksPage));
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseList getFinishedBooks(Pageable pageable) {
        Page<MemberBook> memberBooksPage = memberBookRepository.findByMemberIdAndProgressIsGreaterThanEqual(
                memberUtil.getCurrentMember().getId(), (float) completionThreshold, pageable);

        List<BookResponse> finishedBooks = memberBooksPage.stream()
                .map(MemberBook::getBook)
                .map(BookResponse::fromEntity)
                .toList();

        return BookResponseList.of(finishedBooks, PageInfoDto.from(memberBooksPage));
    }
}
