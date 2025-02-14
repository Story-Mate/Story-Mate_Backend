package store.storymate.storymatebackend.reading.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.reading.domain.Bookmark;
import store.storymate.storymatebackend.reading.domain.MemberBook;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMemberBook(MemberBook memberBook);
}
