package store.storymate.storymatebackend.reading.api.dto.response;

import java.util.List;
import store.storymate.storymatebackend.reading.domain.Book;

public record BookRecommendResponse(Long id, String title, String description, List<String> tags) {
    public static BookRecommendResponse fromEntity(Book book) {
        return new BookRecommendResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getBookTags().stream()
                        .map(bookTag -> bookTag.getTag().getName())
                        .toList()
        );
    }
}
