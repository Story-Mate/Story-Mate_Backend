package store.storymate.storymatebackend.member.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.storymate.storymatebackend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInfoOauthProviderAndOauthInfoOauthId(
            String oauthProvider, String oauthId);
}
