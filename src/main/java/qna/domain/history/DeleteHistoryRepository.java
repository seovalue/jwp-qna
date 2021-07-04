package qna.domain.history;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.history.DeleteHistory;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
