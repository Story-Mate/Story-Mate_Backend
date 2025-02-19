package store.storymate.storymatebackend.reading.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.reading.api.dto.SearchType;
import store.storymate.storymatebackend.reading.api.dto.SortType;
import store.storymate.storymatebackend.reading.api.dto.BookCriteria;
import store.storymate.storymatebackend.reading.domain.Book;
import store.storymate.storymatebackend.reading.domain.Genre;
import store.storymate.storymatebackend.reading.domain.QBook;
import store.storymate.storymatebackend.reading.strategy.SearchStrategy;
import store.storymate.storymatebackend.reading.strategy.SearchStrategyFactory;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> searchBooks(BookCriteria criteria, Pageable pageable) {
        QBook book = QBook.book;
        String query = criteria.getQuery();
        SearchType searchType = criteria.getSearchType();
        String genre = criteria.getGenre().name();
        SortType sortType = criteria.getSortType();

        // BooleanBuilder 초기화 (검색어가 없는 경우에도 대비)
        BooleanBuilder finalPredicate = new BooleanBuilder();

        // 검색어가 있는 경우에만 검색 전략 실행
        if (query != null && !query.trim().isEmpty()) {
            SearchStrategy searchStrategy = SearchStrategyFactory.getStrategy(searchType);
            BooleanBuilder searchPredicate = searchStrategy.buildPredicate(book, query, queryFactory);
            finalPredicate.and(searchPredicate);
        }

        // 장르 필터링 없으면 NONE으로 설정
        if (!genre.trim().isEmpty() && !genre.equals("NONE")) {
            finalPredicate.and(book.genre.eq(Genre.valueOf(genre.toUpperCase())));
        }

        // 정렬 조건 설정
        String sortField = sortType.getField();
        PathBuilder<Book> bookPath = new PathBuilder<>(Book.class, "book");

        Order order = Order.ASC;
        OrderSpecifier<?> orderSpecifier;
        if (sortField.equals("createdAt") || sortField.equals("publishedAt")) {
            orderSpecifier = new OrderSpecifier<>(order, bookPath.getDateTime(sortField, java.time.LocalDateTime.class)); // 날짜 필드 정렬
        } else { // viewCount 같은 숫자 필드 정렬
            orderSpecifier = new OrderSpecifier<>(order, bookPath.getNumber(sortField, Integer.class));
//            orderSpecifier = new OrderSpecifier<>(order, bookPath.getString(sortField)); // 문자열 필드 정렬
        }

        // Query 실행
        List<Book> bookList = queryFactory
                .selectFrom(book)
                .where(finalPredicate)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .select(book.count())
                .from(book)
                .where(finalPredicate)
                .fetchOne();

        return PageableExecutionUtils.getPage(bookList, pageable, () -> total);
    }

    @Override
    public Optional<Book> getRecommendedBookForUser(Long userId) {
        QBook book = QBook.book;

        List<Book> topBooks = queryFactory
                .selectFrom(book)
                .orderBy(book.viewCount.desc())
                .limit(1)
                .fetch();

        // TODO 초기 로직: 단순히 가장 조회수 높은 책 추천, 향후 사용자 로그 분석 기반 알고리즘으로 확장
        if (!topBooks.isEmpty()) {
            return Optional.of(topBooks.get(0));
        }
        return Optional.empty();
    }
}
