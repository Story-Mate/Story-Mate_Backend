package store.storymate.storymatebackend.reading.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.reading.api.dto.PageInfoDto;
import store.storymate.storymatebackend.reading.api.dto.BookCriteria;
import store.storymate.storymatebackend.reading.api.dto.response.BookDetailResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponse;
import store.storymate.storymatebackend.reading.api.dto.response.BookResponseList;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.repository.BookRepository;
import store.storymate.storymatebackend.reading.exception.BookNotFoundException;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponseList getBooks(String query, String searchType, String genre, String sortType,
                                     Pageable pageable) {
        Page<Book> booksPage = bookRepository.searchBooks(new BookCriteria(query, searchType, genre, sortType), pageable);
        List<BookResponse> books = booksPage.map(BookResponse::fromEntity).stream().toList();
        PageInfoDto page = PageInfoDto.from(booksPage);

        return BookResponseList.of(books, page);
    }

    @Override
    public BookDetailResponse getBookDetails(Long bookId) {
        return bookRepository.findById(bookId)
                .map(BookDetailResponse::fromEntity)
                .orElseThrow(BookNotFoundException::new);
    }
}
