package store.storymate.storymatebackend.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import store.storymate.storymatebackend.global.error.exception.UnauthorizedException;

@Component
public class SecurityUtil {

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return Long.parseLong(authentication.getName());
        } catch (Exception e) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
    }

    public String getCurrentMemberRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return authentication.getAuthorities().stream().findFirst().get().getAuthority();
        } catch (Exception e) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }
    }
}
