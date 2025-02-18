package store.storymate.storymatebackend.reading.domain;


import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {
    FAIRY_TALE("동화"),
    SHORT_STORY("단편소설"),
    NOVEL("중장편소설"),
    NONE("없음");

    private final String kor;

    public static Genre of(String requestGenre) {
        return Arrays.stream(values())
                .filter(genre -> genre.name().equalsIgnoreCase(requestGenre))
                .findFirst()
                .orElse(NONE);
    }
}
