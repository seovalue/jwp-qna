package qna.domain.question;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = new User("joanne", "1111", "minjeong", "seominjeong.dev@gmail.com");
        users.save(writer);
    }

    @AfterEach
    void tearDown() {
        questions.deleteAll();
    }

    @Test
    @DisplayName("질문 생성 - 성공")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question("Title", "Contents").writeBy(writer);

        // when
        final Question savedQuestion = questions.save(question);

        // then
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getCreatedAt()).isAfter(now);
        assertThat(savedQuestion.getUpdatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("질문 수정 - 성공")
    void update() {
        // given
        Question question = new Question("Title", "Contents").writeBy(writer);
        final Question savedQuestion = questions.save(question);
        final Long id = savedQuestion.getId();

        // when
        savedQuestion.update("Title-Update", "Contents-Update");

        // then
        assertThat(questions.findById(id).isPresent()).isTrue();
        assertThat(questions.findById(id).get().getTitle()).isEqualTo("Title-Update");
    }

    @Test
    @DisplayName("질문 삭제 - 성공")
    void delete() {
        // given
        Question question = new Question("Title", "Contents").writeBy(writer);
        final Question savedQuestion = questions.save(question);
        final Long id = savedQuestion.getId();

        // when
        questions.delete(savedQuestion);

        // then
        assertThat(questions.findById(id).isPresent()).isFalse();
    }
}