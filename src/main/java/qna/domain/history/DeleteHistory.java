package qna.domain.history;

import org.springframework.data.annotation.CreatedDate;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
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
