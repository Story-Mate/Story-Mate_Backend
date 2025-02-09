package store.storymate.storymatebackend.chatting.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 43631176L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final store.storymate.storymatebackend.characters.domain.QCharacters characters;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> liking = createNumber("liking", Integer.class);

    public final store.storymate.storymatebackend.member.domain.QMember member;

    public final ListPath<ChatMessage, QChatMessage> messages = this.<ChatMessage, QChatMessage>createList("messages", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    public final EnumPath<store.storymate.storymatebackend.global.domain.Status> status = createEnum("status", store.storymate.storymatebackend.global.domain.Status.class);

    public final StringPath title = createString("title");

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.characters = inits.isInitialized("characters") ? new store.storymate.storymatebackend.characters.domain.QCharacters(forProperty("characters")) : null;
        this.member = inits.isInitialized("member") ? new store.storymate.storymatebackend.member.domain.QMember(forProperty("member")) : null;
    }

}

