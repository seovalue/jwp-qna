package qna.domain.question;

import qna.domain.BaseEntity;
import qna.domain.answer.Answer;
import qna.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    private String contents;

    private Long writerId;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return super.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + super.getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writerId +
                ", deleted=" + deleted +
                '}';
    }
}
