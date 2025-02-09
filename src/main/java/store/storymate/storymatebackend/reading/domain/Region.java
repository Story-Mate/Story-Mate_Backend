package store.storymate.storymatebackend.reading.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
    DOMESTIC("국내"),
    OVERSEAS("해외");

    private final String kor;
}
