package store.storymate.storymatebackend.quiz.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.storymate.storymatebackend.global.util.MemberUtil;
import store.storymate.storymatebackend.member.domain.Member;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizAnswerReqDto;
import store.storymate.storymatebackend.quiz.api.dto.request.QuizQuestionReqDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizAnswerResDto;
import store.storymate.storymatebackend.quiz.api.dto.response.QuizQuestionResDto;
import store.storymate.storymatebackend.quiz.domain.CorrectAnswerType;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private final QuizApiClient quizApiClient;
    private final MemberUtil memberUtil;

    // AI 문제 받는 기능
    public QuizQuestionResDto callAiQuestionApi(QuizQuestionReqDto quizQuestionReqDto) {
        return quizApiClient.fetchQuizQuestion(quizQuestionReqDto);
    }

    @Transactional
    public QuizAnswerResDto callAiAnswerApi(QuizAnswerReqDto quizAnswerReqDto) {
        QuizAnswerResDto response = quizApiClient.evaluateQuizAnswer(quizAnswerReqDto);

        if (response != null) {
            Member member = memberUtil.getCurrentMember();
            CorrectAnswerType answerType = CorrectAnswerType.fromString(response.correct());

            if (answerType != null) {
                member.addMessageCount(answerType.getPoints());
            }
        }

        return response;
    }
}
