package store.storymate.storymatebackend.reading.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.Genre;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Page<Book> findByGenre(Genre genre, Pageable pageable);

    Optional<Book> findBookByTitle(String title);
}
