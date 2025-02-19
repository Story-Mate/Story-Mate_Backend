package store.storymate.storymatebackend.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterBirthDateRequestDto(
        @NotBlank String birthDate
) {
}
