package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = new User("joanne", "1111", "minjeong", "seominejeong.dev@gmail.com");
        question = new Question("I have a question", "BlahBlahBlah").writeBy(user);

        // 영속 상태로 만들기
        users.save(user);
        questions.save(question);
    }

    @AfterEach
    void tearDown() {
        answers.deleteAll();
    }

    @Test
    @DisplayName("Answer 생성 - 성공")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Answer answer = new Answer(user, question, "Answer is..");

        // when
        // then
        assertThat(answer.getCreatedAt()).isNull();
        assertThat(answer.getUpdatedAt()).isNull();

        final Answer savedAnswer = answers.save(answer);

        assertThat(savedAnswer).isEqualTo(answer);
        assertThat(savedAnswer.getCreatedAt()).isAfter(now);
        assertThat(savedAnswer.getUpdatedAt()).isAfter(now);
        assertThat(savedAnswer.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer 삭제 - 성공")
    void delete() {
        // given
        Answer answer = new Answer(user, question, "Answer is..");
        final Answer savedAnswer = answers.save(answer);
        final Long id = savedAnswer.getId();

        // when
        answers.delete(savedAnswer);

        // then
        assertThat(answers.findById(id).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Answer 수정 - 성공")
    void update() {
        // given
        Answer answer = new Answer(user, question, "Answer is..");
        final Answer savedAnswer = answers.save(answer);
        final Long id = savedAnswer.getId();

        // when
        savedAnswer.updateContents("Answer is updated.");

        // then
        assertThat(answers.findById(id).isPresent()).isTrue();
        assertThat(answers.findById(id).get().getContents()).isEqualTo("Answer is updated.");
    }
}