package store.storymate.storymatebackend.characters.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.storymate.storymatebackend.characters.api.dto.request.CharactersReqDto;
import store.storymate.storymatebackend.characters.application.CharactersService;
import store.storymate.storymatebackend.chatting.api.dto.request.ChatMessageReqDto;
import store.storymate.storymatebackend.chatting.api.dto.response.ChatMessageResList;
import store.storymate.storymatebackend.chatting.application.ChatMessageService;
import store.storymate.storymatebackend.global.template.ApiResponseTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/characters")
public class CharactersController {

    private final CharactersService charactersService;

    @PostMapping("")
    public ApiResponseTemplate<String> saveCharacters(
            @RequestBody CharactersReqDto charactersReqDto) {
        charactersService.saveCharacters(charactersReqDto);

        return ApiResponseTemplate.ok("캐릭터 저장", "저장 성공");
    }
}