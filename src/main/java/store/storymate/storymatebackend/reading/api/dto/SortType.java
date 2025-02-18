package store.storymate.storymatebackend.reading.api.dto;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    LATEST("최신순", "createdAt"),
    POPULAR("인기순", "viewCount"),
    RECOMMEND("추천순", "publishedAt"),
    NONE("없음", "createdAt");

    private final String kor;
    private final String field;

    public static String getMappedField(String requestedSortType) {
        return Arrays.stream(values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(requestedSortType))
                .findFirst()
                .map(SortType::getField)
                .orElse(NONE.getField());
    }

    public static SortType of(String requestedSortType) {
        return Arrays.stream(values())
                .filter(sortType -> sortType.name().equalsIgnoreCase(requestedSortType))
                .findFirst()
                .orElse(NONE);
    }
}
