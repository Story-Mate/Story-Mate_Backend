package store.storymate.storymatebackend.reading.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberBook is a Querydsl query type for MemberBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBook extends EntityPathBase<MemberBook> {

    private static final long serialVersionUID = -2126283752L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberBook memberBook = new QMemberBook("memberBook");

    public final store.storymate.storymatebackend.global.domain.QBaseEntity _super = new store.storymate.storymatebackend.global.domain.QBaseEntity(this);

    public final QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Float> progress = createNumber("progress", Float.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberBook(String variable) {
        this(MemberBook.class, forVariable(variable), INITS);
    }

    public QMemberBook(Path<? extends MemberBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberBook(PathMetadata metadata, PathInits inits) {
        this(MemberBook.class, metadata, inits);
    }

    public QMemberBook(Class<? extends MemberBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
    }

}

