package store.storymate.storymatebackend.reading.strategy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import store.storymate.storymatebackend.reading.domain.QBook;
import store.storymate.storymatebackend.reading.domain.QBookTag;
import store.storymate.storymatebackend.reading.domain.QTag;

public class TagSearchStrategy implements SearchStrategy {
    @Override
    public BooleanBuilder buildPredicate(QBook book, String query, JPAQueryFactory queryFactory) {
        QBookTag bookTag = QBookTag.bookTag;
        QTag tag = QTag.tag;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(book.id.in(
                queryFactory.select(bookTag.book.id)
                        .from(bookTag)
                        .join(bookTag.tag, tag)
                        .where(tag.name.containsIgnoreCase(query))
        ));
        return builder;
    }
}
