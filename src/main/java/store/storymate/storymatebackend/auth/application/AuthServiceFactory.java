package store.storymate.storymatebackend.auth.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.error.exception.BadRequestException;

@Service
@Transactional(readOnly = true)
public class AuthServiceFactory {
    private final Map<String, AuthService> authServiceMap;

    @Autowired
    public AuthServiceFactory(List<AuthService> authServices) {
        authServiceMap = new HashMap<>();
        for (AuthService authService : authServices) {
            authServiceMap.put(authService.getProvider(), authService);
        }
    }

    public AuthService getAuthService(String provider) {
        return Optional.ofNullable(authServiceMap.get(provider))
                .orElseThrow(() -> new BadRequestException("Error: 알 수 없는 provider: " + provider));
    }
}

