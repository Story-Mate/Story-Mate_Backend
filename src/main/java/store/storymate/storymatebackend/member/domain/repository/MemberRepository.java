package store.storymate.storymatebackend.member.domain.repository;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.storymate.storymatebackend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInfoOauthProviderAndOauthInfoOauthId(
            String oauthProvider, String oauthId);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.messageCount = m.messageCount - 1 WHERE m.id = :memberId AND m.messageCount > 0")
    void decrementMessageCount(@Param("memberId") Long memberId);
}
