package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {
    @Test
    @DisplayName("DeleteHistory 생성 - 성공")
    void create() {
        // given
        User user = new User(1L, "joanne", "1111", "mj", "mj@n.c");
        Question question = new Question(1L, "title", "content").writeBy(user);
        Answer firstAnswer = new Answer(user, question, "first answer");
        Answer secondAnswer = new Answer(user, question, "second answer");

        // when
        question.addAnswer(firstAnswer);
        question.addAnswer(secondAnswer);
        question.delete(user);

        // then
        final List<DeleteHistory> deleteHistories = new DeleteHistory().getDeleteHistory(question);
        assertThat(deleteHistories.size()).isEqualTo(3);
    }
}