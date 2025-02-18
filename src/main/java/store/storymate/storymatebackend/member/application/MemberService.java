package store.storymate.storymatebackend.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.api.dto.MemberResponse;
import store.storymate.storymatebackend.member.api.dto.MemberUpdateRequest;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberUtil memberUtil;

    /**
     * 회원 정보 조회
     */
    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo() {
        Member currentMember = memberUtil.getCurrentMember();
        return MemberResponse.fromEntity(currentMember);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateMemberInfo(MemberUpdateRequest memberUpdateRequest) {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.getOauthInfo().updateNickname(memberUpdateRequest.nickname());
        currentMember.updateBirthDate(memberUpdateRequest.birthDate());

    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember() {
        Member currentMember = memberUtil.getCurrentMember();
        currentMember.updateStatus(Status.DELETED);
    }
}
