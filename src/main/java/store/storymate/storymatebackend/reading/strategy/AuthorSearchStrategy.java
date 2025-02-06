package store.storymate.storymatebackend.reading.strategy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import store.storymate.storymatebackend.reading.domain.QBook;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public BooleanBuilder buildPredicate(QBook book, String query, JPAQueryFactory queryFactory) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(book.author.containsIgnoreCase(query));
        return builder;
    }
}
