package store.storymate.storymatebackend.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private DateUtil() {
        throw new UnsupportedOperationException("이 클래스는 유틸리티 클래스이며 인스턴스화할 수 없습니다.");
    }

    public static LocalDate parseToLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            throw new BadRequestException("날짜는 null이나 빈 값일 수 없습니다.");
        }

        try {
            return LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("잘못된 날짜 형식입니다. yyyy.MM.dd 형식이어야 합니다: " + dateString);
        }
    }
}
