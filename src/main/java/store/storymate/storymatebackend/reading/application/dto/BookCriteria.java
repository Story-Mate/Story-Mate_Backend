package store.storymate.storymatebackend.reading.application.dto;

import lombok.Getter;
import store.storymate.storymatebackend.reading.api.dto.SearchType;
import store.storymate.storymatebackend.reading.api.dto.SortType;
import store.storymate.storymatebackend.reading.domain.Genre;

@Getter
public class BookCriteria {
    private final String query;
    private final SearchType searchType;
    private final Genre genre;
    private final SortType sortType;

    public BookCriteria(String query, String searchType, String genre, String sortType) {
        this.query = query;
        this.searchType = SearchType.getSearchType(searchType);
        this.genre = Genre.of(genre);
        this.sortType = SortType.getSortType(sortType);
    }
}