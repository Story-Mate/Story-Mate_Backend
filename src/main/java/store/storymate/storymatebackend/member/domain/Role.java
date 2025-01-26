package store.storymate.storymatebackend.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("일반 사용자"),
    ;

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
