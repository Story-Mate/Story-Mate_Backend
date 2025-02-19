package store.storymate.storymatebackend.member.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;
import store.storymate.storymatebackend.member.application.MemberService;
import store.storymate.storymatebackend.member.dto.request.RegisterBirthDateRequestDto;
import store.storymate.storymatebackend.member.dto.response.MemberInfoResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberDocs {
    private final MemberService memberService;

    @Override
    @PostMapping("/register-birth-date")
    public ApiResponseTemplate<MemberInfoResponseDto> registerBirthDate(@RequestBody RegisterBirthDateRequestDto request) {

        return ApiResponseTemplate.ok("등록이 완료되었습니다.", memberService.registerBirthDate(request));
    }
}
