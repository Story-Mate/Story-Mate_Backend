package store.storymate.storymatebackend.reading.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 2063619614L;

    public static final QBook book = new QBook("book");

    public final store.storymate.storymatebackend.global.domain.QBaseEntity _super = new store.storymate.storymatebackend.global.domain.QBaseEntity(this);

    public final StringPath author = createString("author");

    public final ListPath<BookTag, QBookTag> bookTags = this.<BookTag, QBookTag>createList("bookTags", BookTag.class, QBookTag.class, PathInits.DIRECT2);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final EnumPath<Genre> genre = createEnum("genre", Genre.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> pageCount = createNumber("pageCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> publishedAt = createDateTime("publishedAt", java.time.LocalDateTime.class);

    public final EnumPath<Region> region = createEnum("region", Region.class);

    public final StringPath title = createString("title");

    public final StringPath translator = createString("translator");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

