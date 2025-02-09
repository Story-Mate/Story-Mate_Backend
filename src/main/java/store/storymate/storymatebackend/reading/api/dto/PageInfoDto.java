package store.storymate.storymatebackend.reading.api.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfoDto(
        int pageNumber,    // 현재 페이지 번호
        int pageSize,    // 페이지당 데이터 개수
        long totalElements, // 전체 데이터 수
        int totalPages,   // 전체 페이지 수
        boolean last    // 마지막 페이지 여부
) {
    public static PageInfoDto from(Page<?> entityPage) {
        return PageInfoDto.builder()
                .pageNumber(entityPage.getNumber())
                .pageSize(entityPage.getSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .last(entityPage.isLast())
                .build();
    }
}
