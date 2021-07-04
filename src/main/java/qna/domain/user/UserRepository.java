package qna.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
