package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoryTest {
    @Test
    @DisplayName("DeleteHistory 생성 - 성공")
    void create() {
        // given
        User user = new User(1L, "joanne", "1111", "mj", "mj@n.c");
        Question question = new Question(1L, "title", "content");
        Answer firstAnswer = new Answer(user, question, "first answer");
        Answer secondAnswer = new Answer(user, question, "second answer");

        // when
        question.addAnswer(firstAnswer);
        question.addAnswer(secondAnswer);

        // then
    }
}