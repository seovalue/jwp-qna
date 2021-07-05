package qna.domain.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.domain.answer.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.user.UserFixture.JAVAJIGI;
import static qna.domain.user.UserFixture.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    private Answer firstAnswer;
    private Answer secondAnswer;

    @BeforeEach
    void setUp() {
        firstAnswer = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        secondAnswer = new Answer(SANJIGI, Q2, "Answers Contents2");
    }

    @DisplayName("질문에 답변을 추가 - 성공")
    @Test
    void addAnswer() {
        Q1.addAnswer(firstAnswer);

        assertTrue(Q1.getAnswers().contains(firstAnswer));
        assertThat(firstAnswer.getQuestion()).isEqualTo(Q1);
    }

    @DisplayName("자신이 작성한 질문을 삭제 -  성공")
    @Test
    void delete() {
        Q1.delete(JAVAJIGI);
        Q2.delete(SANJIGI);

        assertTrue(Q1.isDeleted());
        assertTrue(Q2.isDeleted());
    }

    @DisplayName("자신이 작성하지 않은 질문을 삭제 - 실패")
    @Test
    void deleteWhenNotWrite() {
        assertThatThrownBy(() -> Q1.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> Q2.delete(JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
    }

}
