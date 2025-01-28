package store.storymate.storymatebackend.auth.application;

import store.storymate.storymatebackend.auth.api.dto.response.IdTokenResponseDto;
import store.storymate.storymatebackend.auth.api.dto.response.MemberInfoResponseDto;

public interface AuthService {
    MemberInfoResponseDto getUserInfo(String authCode);

    String getProvider();

    IdTokenResponseDto getIdToken(String code);
}
