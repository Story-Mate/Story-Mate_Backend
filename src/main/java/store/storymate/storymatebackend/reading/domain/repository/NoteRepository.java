package store.storymate.storymatebackend.reading.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.reading.domain.Note;
import store.storymate.storymatebackend.reading.domain.MemberBook;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByMemberBook(MemberBook memberBook);
}
