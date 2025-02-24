package store.storymate.storymatebackend.quiz.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CorrectAnswerType {
    O(3L),
    C(1L),
    TRUE(2L);

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
