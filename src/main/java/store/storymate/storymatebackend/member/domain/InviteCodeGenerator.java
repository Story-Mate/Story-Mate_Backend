package store.storymate.storymatebackend.member.domain;

import java.util.concurrent.ThreadLocalRandom;

class InviteCodeGenerator {

    // 초대 코드 규정이 없기 때문에 일단 임시로 알파벳 대소문자 + 숫자 조합
    // 일단 고유 초대 코드는 고려하지 않음
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    static String generateInviteCode() {
        StringBuilder inviteCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            inviteCode.append(CHARACTERS.charAt(randomIndex));
        }

        return inviteCode.toString();
    }
}


