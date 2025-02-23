package store.storymate.storymatebackend.member.application;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.util.DateUtil;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.api.dto.MemberResponse;
import store.storymate.storymatebackend.member.api.dto.MemberUpdateRequest;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;
import store.storymate.storymatebackend.member.dto.request.RegisterBirthDateRequestDto;
import store.storymate.storymatebackend.member.dto.response.MemberInfoResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberUtil memberUtil;


    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberResponse.fromEntity(currentMember);
    }

    @Transactional
    public void updateMemberInfo(MemberUpdateRequest memberUpdateRequest) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.getOauthInfo().updateNickname(memberUpdateRequest.nickname());
        currentMember.updateBirthDate(LocalDate.parse(memberUpdateRequest.birthDate()));

    }

    @Transactional
    public void deleteMember() {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.updateStatus(Status.DELETED);
    }

    @Transactional
    public MemberInfoResponseDto registerBirthDate(@Valid RegisterBirthDateRequestDto request) {
        LocalDate birthDate = DateUtil.parseToLocalDate(request.birthDate());

        Member member = memberUtil.getCurrentMember();
        member.updateBirthDate(birthDate);

        return MemberInfoResponseDto.of(member);
    }

    @Transactional
    public void decreaseMessageCount(Long memberId) {
        memberRepository.decrementMessageCount(memberId);
    }
}
