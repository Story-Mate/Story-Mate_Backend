package store.storymate.storymatebackend.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.member.api.dto.MemberResponse;
import store.storymate.storymatebackend.member.api.dto.MemberUpdateRequest;
import store.storymate.storymatebackend.member.dto.request.RegisterBirthDateRequestDto;
import store.storymate.storymatebackend.member.dto.response.MemberInfoResponseDto;

@Tag(name = "[회원 API]", description = "회원 관련 API")
public interface MemberDocs {

    @Operation(summary = "생년월일 등록", description = "사용자의 생년월일을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생년월일 등록 성공",
                            content = @Content(schema = @Schema(implementation = MemberInfoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<MemberInfoResponseDto> registerBirthDate(
            @Parameter(description = "생년월일 등록 요청 DTO", required = true)
            RegisterBirthDateRequestDto request
    );

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = MemberResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<MemberResponse> getMemberInfo();

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<Void> updateMemberInfo(
            @Parameter(description = "회원 정보 수정 요청 DTO", required = true)
            MemberUpdateRequest memberUpdateRequest
    );

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ApiResponseTemplate<Void> deleteMember();
}
