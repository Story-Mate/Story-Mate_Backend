package store.storymate.storymatebackend.reading.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.reading.api.dto.BookCriteria;
import store.storymate.storymatebackend.reading.api.dto.PageInfoDto;
import store.storymate.storymatebackend.reading.api.dto.response.BookDetailResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;
import store.storymate.storymatebackend.reading.exception.BookNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponseList getBooks(BookCriteria criteria, Pageable pageable) {
        Page<Book> booksPage = bookRepository.searchBooks(criteria, pageable);
        List<BookResponse> books = booksPage.map(BookResponse::fromEntity).stream().toList();
        PageInfoDto page = PageInfoDto.from(booksPage);

        return BookResponseList.of(books, page);
    }

    @Override
    public BookDetailResponse getBookDetails(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        String tags = "";
        if (book.getBookTags() != null) {
            tags = book.getBookTags().stream()
                    .map(bookTag -> "#" + bookTag.getTag().getName())
                    .reduce("", (a, b) -> a + b);
        }
        return BookDetailResponse.fromEntity(book, tags);
    }
}
