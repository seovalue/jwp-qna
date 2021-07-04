package qna.domain.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.question.Question;
import qna.domain.question.QuestionTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.user.UserTest.JAVAJIGI;
import static qna.domain.user.UserTest.SANJIGI;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, QuestionTest.Q1, "Answers Contents2");
    private Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    private Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    private Answer firstAnswer;
    private Answer secondAnswer;

    @BeforeEach
    void setUp() {
        firstAnswer = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        secondAnswer = new Answer(SANJIGI, Q1, "Answers Contents2");
    }

    @DisplayName("답변에 대한 작성자 확인 - 성공")
    @Test
    void isOwner() {
        assertTrue(firstAnswer.isOwner(JAVAJIGI));
        assertTrue(secondAnswer.isOwner(SANJIGI));
    }

    @DisplayName("문제 할당 - 성공")
    @Test
    void toQuestion() {
        firstAnswer.toQuestion(Q2);

        assertThat(firstAnswer.getQuestion()).isEqualTo(Q2);
    }

    @DisplayName("자신이 작성한 문제를 삭제 - 성공")
    @Test
    void delete() {
        firstAnswer.delete(JAVAJIGI);
        assertTrue(firstAnswer.isDeleted());
    }

    @DisplayName("자신이 작성하지 않은 문제를 삭제 - 실패")
    @Test
    void deleteFail() {
        assertThatThrownBy(() -> firstAnswer.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
