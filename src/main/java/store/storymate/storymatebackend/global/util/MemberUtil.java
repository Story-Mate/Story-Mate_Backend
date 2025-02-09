package store.storymate.storymatebackend.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.error.exception.NotFoundException;
import store.storymate.storymatebackend.global.error.exception.UnauthorizedException;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;


@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getCurrentMember() {
        return memberRepository
                .findById(securityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Member getMemberByMemberId(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    public String getMemberRole() {
        String role = securityUtil.getCurrentMemberRole();
        if (role == null) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
        return role;
    }
}
