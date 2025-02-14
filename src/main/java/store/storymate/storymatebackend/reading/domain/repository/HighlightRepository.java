package store.storymate.storymatebackend.reading.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.reading.domain.Highlight;
import store.storymate.storymatebackend.reading.domain.MemberBook;

@Repository
public interface HighlightRepository extends JpaRepository<Highlight, Long> {
    List<Highlight> findByMemberBook(MemberBook memberBook);
}
