package qna.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    @AfterEach
    void tearDown() {
        users.deleteAll();
    }

    @Test
    @DisplayName("User 생성 - 성공")
    void create() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = new User("joanne", "1111", "minjeong", "seominejeong.dev@gmail.com");

        // when
        // then
        User result = users.save(user);

        assertThat(result).isSameAs(user);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isAfter(now);
        assertThat(result.getUpdatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("User 수정 - 성공")
    void update() {
        // given
        User user = new User("joanne", "1111", "minjeong", "seominejeong.dev@gmail.com");
        User updatedUser = new User("joanne", "1111", "minjeong", "mathmjseo@khu.ac.kr");

        // when
        User result = users.save(user);
        result.update(user, updatedUser);

        // then
        assertThat(result.equalsNameAndEmail(updatedUser)).isTrue();
    }

    @Test
    @DisplayName("User 삭제 - 성공")
    void delete() {
        // given
        User user = new User("joanne", "1111", "minjeong", "seominejeong.dev@gmail.com");
        final User savedUser = users.save(user);
        final Long id = savedUser.getId();

        // when
        users.delete(user);

        // then
        assertThat(users.findById(id).isPresent()).isFalse();
    }
}