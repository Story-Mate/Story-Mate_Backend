package store.storymate.storymatebackend.reading.strategy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import store.storymate.storymatebackend.reading.domain.QBook;

public interface SearchStrategy {
    BooleanBuilder buildPredicate(QBook book, String query, JPAQueryFactory queryFactory);
}
