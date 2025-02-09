package store.storymate.storymatebackend.reading.api.dto.response;

import java.util.List;
import store.storymate.storymatebackend.reading.domain.Book;

public record BookResponse(Long id, String title, List<String> tags) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getBookTags().stream()
                        .map(bookTag -> bookTag.getTag().getName())
                        .toList()
        );
    }
}
