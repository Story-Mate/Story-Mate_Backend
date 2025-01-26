package store.storymate.storymatebackend.auth.application;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.auth.api.dto.response.MemberInfoResponseDto;
import store.storymate.storymatebackend.auth.api.dto.response.MemberLoginResponseDto;
import store.storymate.storymatebackend.global.domain.Status;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;
import store.storymate.storymatebackend.global.error.exception.NotFoundException;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.member.domain.SocialType;
import store.storymate.storymatebackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthMemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberLoginResponseDto saveMemberInfo(MemberInfoResponseDto memberInfoResponseDTO, SocialType provider) {
        validateEmail(memberInfoResponseDTO.email());

        Member member = findOrCreateMember(memberInfoResponseDTO, provider);

        validateSocialType(provider, member);

        return MemberLoginResponseDto.from(member);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new NotFoundException("이메일을 찾을 수 없습니다.");
        }
    }

    private Member findOrCreateMember(MemberInfoResponseDto memberInfoResponseDTO, SocialType provider) {
        return memberRepository.findByEmail(memberInfoResponseDTO.email())
                .orElseGet(() -> createMember(memberInfoResponseDTO, provider));
    }

    private void validateSocialType(SocialType provider, Member member) {
        if (!provider.equals(member.getSocialType())) {
            throw new BadRequestException("이미 존재하는 이메일입니다.");
        }
    }

    private Member createMember(MemberInfoResponseDto memberInfoResponseDTO, SocialType provider) {
        String memberPicture = resolveMemberPicture(memberInfoResponseDTO.picture());

        return memberRepository.save(buildNewMember(memberInfoResponseDTO, provider, memberPicture));
    }

    private String resolveMemberPicture(String picture) {
        return Optional.ofNullable(picture)
                .map(this::convertToHighRes)
                .orElseThrow();
    }

    private String convertToHighRes(String url) {
        return url.replace("s96-c", "s2048-c");
    }

    private Member buildNewMember(MemberInfoResponseDto memberInfoResponseDTO, SocialType provider, String memberPicture) {
        return Member.builder()
                .email(memberInfoResponseDTO.email())
                .name(memberInfoResponseDTO.nickname())
                .profileImageUrl(memberPicture)
                .socialType(provider)
                .age(10)
                .firstLogin(true)
                .status(Status.ACTIVE)
                .build();
    }

    private static int calculateAgeFromBirthYear(String birthYear) {
        int currentYear = LocalDate.now().getYear();
        int birthYearInt = Integer.parseInt(birthYear);

        return currentYear - birthYearInt;
    }
}
