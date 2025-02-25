package store.storymate.storymatebackend.quiz.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CorrectAnswerType {
    O(5L),
    C(3L),
    TRUE(3L);

    private final long points;

    CorrectAnswerType(long points) {
        this.points = points;
    }

    public static CorrectAnswerType fromString(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
