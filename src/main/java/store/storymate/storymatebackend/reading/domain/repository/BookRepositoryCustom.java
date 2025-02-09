package store.storymate.storymatebackend.reading.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.storymate.storymatebackend.reading.api.dto.BookCriteria;
import store.storymate.storymatebackend.reading.domain.Book;

public interface BookRepositoryCustom {
    Page<Book> searchBooks(BookCriteria criteria, Pageable pageable);
    Optional<Book> getRecommendedBookForUser(Long userId);
}
