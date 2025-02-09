package store.storymate.storymatebackend.reading.api.dto;

import lombok.Getter;
import store.storymate.storymatebackend.reading.domain.Genre;

@Getter
public class BookCriteria {
    private final String query;
    private final SearchType searchType;
    private final Genre genre;
    private final SortType sortType;

    public BookCriteria(String query, String searchType, String genre, String sortType) {
        this.query = query;
        this.searchType = SearchType.valueOf(searchType);
        this.genre = Genre.valueOf(genre);
        this.sortType = SortType.valueOf(sortType);
    }
}