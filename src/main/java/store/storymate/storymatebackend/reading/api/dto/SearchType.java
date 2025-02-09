package store.storymate.storymatebackend.reading.api.dto;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {
    TITLE("제목", "title"),
    AUTHOR("저자", "author"),
    TAG("태그", "tag"),
    ALL("전체", "all");

    private final String kor;
    private final String field;

    public static String getMappedField(String requestedSearchType) {
        return Arrays.stream(values())
                .filter(searchType -> searchType.name().equalsIgnoreCase(requestedSearchType))
                .findFirst()
                .map(SearchType::getField)
                .orElse(ALL.getField());
    }

    public static SearchType getSearchType(String requestedSearchType) {
        return Arrays.stream(values())
                .filter(searchType -> searchType.name().equalsIgnoreCase(requestedSearchType))
                .findFirst()
                .orElse(ALL);
    }
}
