package store.storymate.storymatebackend.auth.repository;

import org.springframework.data.repository.CrudRepository;
import store.storymate.storymatebackend.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
