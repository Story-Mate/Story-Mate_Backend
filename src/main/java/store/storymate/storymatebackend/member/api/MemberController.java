package store.storymate.storymatebackend.member.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.member.api.dto.MemberResponse;
import store.storymate.storymatebackend.member.api.dto.MemberUpdateRequest;
import store.storymate.storymatebackend.member.application.MemberService;
import store.storymate.storymatebackend.member.dto.request.RegisterBirthDateRequestDto;
import store.storymate.storymatebackend.member.dto.response.MemberInfoResponseDto;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements MemberDocs {
    private final MemberService memberService;

    @Override
    @PostMapping("/register-birth-date")
    public ApiResponseTemplate<MemberInfoResponseDto> registerBirthDate(
            @RequestBody RegisterBirthDateRequestDto request) {
        return ApiResponseTemplate.ok("등록이 완료되었습니다.", memberService.registerBirthDate(request));
    }

    @Override
    @GetMapping("/info")
    public ApiResponseTemplate<MemberResponse> getMemberInfo() {
        return ApiResponseTemplate.ok("회원 정보 조회 성공", memberService.getMemberInfo());
    }

    @Override
    @PutMapping("/info")
    public ApiResponseTemplate<Void> updateMemberInfo(
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMemberInfo(memberUpdateRequest);
        return ApiResponseTemplate.ok("회원 정보 수정 성공");
    }

    @Override
    @DeleteMapping("/info")
    public ApiResponseTemplate<Void> deleteMember() {
        memberService.deleteMember();
        return ApiResponseTemplate.ok("회원 탈퇴 성공");
    }
}
