package qna.domain.history;

import org.springframework.data.annotation.CreatedDate;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"), name = "delete_by_id")
    private User deletedByUser;

    @CreatedDate
    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    public List<DeleteHistory> getDeleteHistory(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        addAnswersToDeleteHistory(question.getAnswers(), deleteHistories);
        return deleteHistories;
    }

    private void addAnswersToDeleteHistory(List<Answer> answers, List<DeleteHistory> deleteHistories) {
        for (Answer answer : answers) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedBy=" + deletedByUser +
                ", createDate=" + createDate +
                '}';
    }
}
