package store.storymate.storymatebackend.reading.strategy;

import java.util.Map;
import store.storymate.storymatebackend.reading.api.dto.SearchType;

public class SearchStrategyFactory {
    private static final Map<SearchType, SearchStrategy> STRATEGY_MAP = Map.of(
            SearchType.TITLE, new TitleSearchStrategy(),
            SearchType.AUTHOR, new AuthorSearchStrategy(),
            SearchType.TAG, new TagSearchStrategy(),
            SearchType.ALL, new AllSearchStrategy()
    );

    public static SearchStrategy getStrategy(SearchType searchType) {
        return STRATEGY_MAP.getOrDefault(searchType, new AllSearchStrategy()); 
    }
}
