package store.storymate.storymatebackend.member.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.global.util.DateUtil;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;
import store.storymate.storymatebackend.member.dto.request.RegisterBirthDateRequestDto;
import store.storymate.storymatebackend.member.dto.response.MemberInfoResponseDto;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public MemberInfoResponseDto registerBirthDate(@Valid RegisterBirthDateRequestDto request) {
        LocalDate birthDate = DateUtil.parseToLocalDate(request.birthDate());

        Member member = memberUtil.getCurrentMember();
        member.updateBirthDate(birthDate);

        return MemberInfoResponseDto.of(member);
    }
}
