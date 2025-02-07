package store.storymate.storymatebackend.characters.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.storymate.storymatebackend.characters.api.dto.request.CharactersReqDto;
import store.storymate.storymatebackend.characters.domain.repository.CharactersRepository;

@Service
@RequiredArgsConstructor
public class CharactersService {

    private final CharactersRepository charactersRepository;

    @Transactional
    public void saveCharacters(CharactersReqDto charactersReqDto) {
        charactersRepository.save(CharactersReqDto.toEntity(charactersReqDto));
    }
}
