package net.frontlinesms.plugins.learn;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;
import net.frontlinesms.test.spring.ApplicationContextAwareTestCase;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.spring.SpringInitialisedBean;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class LearnIncomingMessageProcessorTest extends ApplicationContextAwareTestCase {
	/** instance under test */
	@SpringInitialisedBean private LearnIncomingMessageProcessor limp;

	@MockBean EventBus eventBus;
	@MockBean AssessmentMessageDao assessmentMessageDao;
	@MockBean AssessmentMessageResponseDao responseDao;
	@MockBean ContactDao contactDao;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
//> TESTS
	public void testStartShouldRegisterWithEventBus() {
		// when
		limp.start();
		
		// then
		verify(eventBus).registerObserver(limp);
	}
	
	public void testShutdownShouldUnregisterWithEventBus() {
		// when
		limp.shutdown();
		
		// then
		verify(eventBus).unregisterObserver(limp);
	}
	
	public void testGetAssessmentMessage() {
		mockAssessmentMessageDao();
		final Object[] PAIRS = {
				33, "33 TRUE",
				56, "56TRUE",
				4747, "4747trUe",
				33, "33 FALSE",
				56, "56FALSE",
				4747, "4747falSe",
				123, "\r\n     123       \r   true",
				
				23, "23 A",
				44, "44 b",
				69, "69C",
				123, "\r\n     123       \r   c"
		};
		for (int i = 0; i < PAIRS.length; i+=2) {
			testGetAssessmentMessage((Integer) PAIRS[i], (String) PAIRS[i+1]);
		}
	}
	
	private void testGetAssessmentMessage(long expectedId, String messageText) {
		AssessmentMessage actualQuestion = limp.getAssessmentMessage(mockMessage(messageText));
		assertNotNull(actualQuestion);
		assertEquals(expectedId, actualQuestion.getId());
	}
	
	public void testProcessMessage() {
		// given
		final String from = "+123456789";
		Question q = mock(Question.class);
		AssessmentMessage am = mockAssessmentMessage(q);
		FrontlineMessage fmessage = mockMessage(from, "123 true");
		Contact student = mockContact(contactDao, from);
		when(assessmentMessageDao.get(123)).thenReturn(am);
		
		// when
		limp.processMessage(fmessage);
		
		// then
		verify(responseDao).save(assessmentMessageResponseWithMessageAndStudentAndAnswerAndCorrect(am, student, 0, true));
	}
	
	public void testGetAssessmentMessageForUnprocessableText() {
		// when
		AssessmentMessage m = limp.getAssessmentMessage(mockMessage("asdf"));
		
		// then
		assertNull(m);
	}
	
	public void testGetAnswer() {
		final Object[] PAIRS = {
			0, "33 TRUE",
			0, "56TRUE",
			0, "4747trUe",
			0, "\r\n     123       \r   true",
			1, "33 FALSE",
			1, "56FALSE",
			1, "4747falSe",
			
			0, "23 A",
			1, "44 b",
			2, "69C",
			2, "\r\n     123       \r   c"
		};
		for (int i = 0; i < PAIRS.length; i+=2) {
			testGetAnswer((Integer) PAIRS[i], (String) PAIRS[i+1]);
		}
	}
	
	private void testGetAnswer(int expected, String messageText) {
		int actual = limp.getAnswer(mockMessage(messageText));
		assertEquals(expected, actual);
	}
	
	public void testGetAnswer_badInput() {
		// when
		int answer = limp.getAnswer(mockMessage("something random"));
		
		// then
		assertEquals(-1, answer);
	}
	
	public void testIncorrectAnswerShouldNotGenerateReplyIfIncorrectResponseSet() {
		TODO();
	}
	
	public void testIncorrectAnswerShouldGenerateReplyIfIncorrectResponseSet() {
		TODO();
	}
	
	public void testCorrectAnswerShouldNotGenerateReplyIfIncorrectResponseSet() {
		TODO();
	}
	
	public void testCorrectAnswerShouldGenerateReplyIfIncorrectResponseSet() {
		TODO();
	}
	
	public void testAnyAnswerReceivedShouldCancelScheduledMessageResend() {
		TODO();
	}
	
//> SETUP
	private void mockAssessmentMessageDao() {
		when(assessmentMessageDao.get(anyLong())).thenAnswer(new Answer<AssessmentMessage>() {
			public AssessmentMessage answer(InvocationOnMock invocation) throws Throwable {
				long id = (Long) invocation.getArguments()[0];
				AssessmentMessage m = mock(AssessmentMessage.class);
				when(m.getId()).thenReturn(id);
				return m;
			}
		});
	}
}
