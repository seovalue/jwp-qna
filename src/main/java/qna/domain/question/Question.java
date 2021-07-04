package qna.domain.question;

import qna.CannotDeleteException;
import qna.domain.BaseEntity;
import qna.domain.answer.Answer;
import qna.domain.history.ContentType;
import qna.domain.history.DeleteHistory;
import qna.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

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
        this.writer = writer;
        return this;
    }

    public Question update(String title, String Contents) {
        this.title = title;
        this.contents = contents;
        return this;
    }

    public void delete(User user){
        validateAuthority(user);
        isAnotherAnswerExist(user);
        this.deleted = true;
        deleteAllChainedAnswers(user);
    }

    private void deleteAllChainedAnswers(User user) {
        answers.forEach(answer -> {answer.delete(user);});
    }

    private void isAnotherAnswerExist(User user) {
        answers.forEach(answer -> {
            if(!answer.isOwner(user)) {
                throw new CannotDeleteException("다른 사람의 답변이 존재하므로 삭제할 수 없습니다.");
            }
        });
    }

    private void validateAuthority(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public User getWriter() {
        return writer;
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
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
