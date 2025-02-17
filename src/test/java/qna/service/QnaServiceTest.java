package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.exception.CannotDeleteException;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.history.ContentType;
import qna.domain.history.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.question.QuestionTest;
import qna.domain.user.UserFixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserFixture.JAVAJIGI);
        answer = new Answer(1L, UserFixture.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        lenient().when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        lenient().when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserFixture.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        lenient().when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserFixture.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        lenient().when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        lenient().when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(UserFixture.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer(2L, UserFixture.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        lenient().when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        lenient().when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer, answer2));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserFixture.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
