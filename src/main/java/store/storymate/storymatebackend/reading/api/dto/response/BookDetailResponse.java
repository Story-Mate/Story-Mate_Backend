package store.storymate.storymatebackend.reading.api.dto.response;

import java.time.format.DateTimeFormatter;
import lombok.Builder;
import store.storymate.storymatebackend.reading.domain.Book;

@Builder
public record BookDetailResponse(
        Long id,
        String title,
        String author,
        String publishedYear,
        String tags,
        String keyword,
        String description
) {
    public static BookDetailResponse fromEntity(Book book, String tags) {
        return BookDetailResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publishedYear(book.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy")))
                .tags(tags)
                .description(book.getDescription())
                .build();
    }
}
