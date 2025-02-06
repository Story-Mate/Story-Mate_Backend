package store.storymate.storymatebackend.global.jwt.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToken extends EntityPathBase<Token> {

    private static final long serialVersionUID = -1012354322L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToken token = new QToken("token");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final store.storymate.storymatebackend.member.domain.QMember member;

    public final StringPath refreshToken = createString("refreshToken");

    public QToken(String variable) {
        this(Token.class, forVariable(variable), INITS);
    }

    public QToken(Path<? extends Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToken(PathMetadata metadata, PathInits inits) {
        this(Token.class, metadata, inits);
    }

    public QToken(Class<? extends Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new store.storymate.storymatebackend.member.domain.QMember(forProperty("member")) : null;
    }

}

