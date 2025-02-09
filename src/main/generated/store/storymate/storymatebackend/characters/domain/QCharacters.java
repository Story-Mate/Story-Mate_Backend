package store.storymate.storymatebackend.characters.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCharacters is a Querydsl query type for Characters
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCharacters extends EntityPathBase<Characters> {

    private static final long serialVersionUID = -28037757L;

    public static final QCharacters characters = new QCharacters("characters");

    public final store.storymate.storymatebackend.global.domain.QBaseEntity _super = new store.storymate.storymatebackend.global.domain.QBaseEntity(this);

    public final ListPath<store.storymate.storymatebackend.chatting.domain.ChatRoom, store.storymate.storymatebackend.chatting.domain.QChatRoom> chatRoom = this.<store.storymate.storymatebackend.chatting.domain.ChatRoom, store.storymate.storymatebackend.chatting.domain.QChatRoom>createList("chatRoom", store.storymate.storymatebackend.chatting.domain.ChatRoom.class, store.storymate.storymatebackend.chatting.domain.QChatRoom.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> like_count = createNumber("like_count", Integer.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCharacters(String variable) {
        super(Characters.class, forVariable(variable));
    }

    public QCharacters(Path<? extends Characters> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCharacters(PathMetadata metadata) {
        super(Characters.class, metadata);
    }

}

