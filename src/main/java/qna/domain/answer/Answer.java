package qna.domain.answer;

import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.domain.BaseEntity;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {

    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        super(id);

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void delete(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("작성자가 아닙니다.");
        }
        this.deleted = true;
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Answer updateContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Long getId() {
        return super.getId();
    }

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + super.getId() +
                ", writer=" + getWriter() +
                ", question" + getQuestion() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
