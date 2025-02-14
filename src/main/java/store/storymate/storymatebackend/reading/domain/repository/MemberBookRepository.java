package store.storymate.storymatebackend.reading.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.reading.domain.MemberBook;

@Repository
public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {
    Optional<MemberBook> findByMemberIdAndBookId(Long memberId, Long bookId);
}
