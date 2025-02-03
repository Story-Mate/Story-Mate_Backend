package store.storymate.storymatebackend.characters.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.storymate.storymatebackend.characters.domain.Characters;

@Repository
public interface CharactersRepository extends JpaRepository<Characters, Long> {
}