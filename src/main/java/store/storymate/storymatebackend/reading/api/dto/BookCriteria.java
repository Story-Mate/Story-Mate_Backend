package store.storymate.storymatebackend.reading.api.dto;

import lombok.Builder;
import lombok.Getter;
import store.storymate.storymatebackend.reading.domain.Genre;

@Getter
public class BookCriteria {
    private final String query;
    private final SearchType searchType;
    private final Genre genre;
    private final SortType sortType;

    @Builder
    public BookCriteria(String query, String searchType, String genre, String sortType) {
        this.query = query;
        this.searchType = SearchType.of(searchType);
        this.genre = Genre.of(genre);
        this.sortType = SortType.of(sortType);
    }
}