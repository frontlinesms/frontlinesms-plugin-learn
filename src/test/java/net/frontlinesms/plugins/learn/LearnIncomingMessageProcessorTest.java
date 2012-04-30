package net.frontlinesms.plugins.learn;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessageResponse;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
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
	@MockBean LearnPluginProperties properties;
	@MockBean FrontlineSMS frontlineController;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		inject(limp, "properties", properties);
		inject(limp, "frontlineController", frontlineController);
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
				56, "56T",
				4747, "4747trUe",
				33, "33 FALSE",
				33, "33 f",
				56, "56FALSE",
				4747, "4747falSe",
				123, "\r\n     123       \r   true",
				33, "33.t",
				33, "33   .t",
				33, "33.   t",
				
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
		AssessmentMessage actualQuestion = limp.getAssessmentMessage(mockIncomingMessage(messageText));
		assertNotNull(actualQuestion);
		assertEquals(expectedId, actualQuestion.getId());
	}
	
	public void testProcessMessage() {
		// given
		final String from = "+123456789";
		Question q = mock(Question.class);
		AssessmentMessage am = mockAssessmentMessage(q);
		FrontlineMessage fmessage = mockIncomingMessage(from, "123 true");
		Contact student = mockContact(contactDao, from);
		when(assessmentMessageDao.get(123)).thenReturn(am);
		
		// when
		limp.processMessage(fmessage);
		
		// then
		verify(responseDao).save(assessmentMessageResponseWithMessageAndStudentAndAnswerAndCorrect(am, student, 0, true));
	}
	
	public void testProcessMessageShouldIgnoreOutgoing() {
		// given
		final String from = "+123456789";
		Question q = mock(Question.class);
		AssessmentMessage am = mockAssessmentMessage(q);
		FrontlineMessage fmessage = mockOutgoingMessage(from, "123 true");
		mockContact(contactDao, from);
		when(assessmentMessageDao.get(123)).thenReturn(am);
		
		// when
		limp.processMessage(fmessage);
		
		// then
		verify(responseDao, never()).save(any(AssessmentMessageResponse.class));
	}
	
	public void testGetAssessmentMessageForUnprocessableText() {
		// when
		AssessmentMessage m = limp.getAssessmentMessage(mockIncomingMessage("asdf"));
		
		// then
		assertNull(m);
	}
	
	public void testGetAnswer() {
		final Object[] PAIRS = {
			0, "33 TRUE",
			0, "56TRUE",
			0, "4747trUe",
			0, "4747t",
			0, "4747 t",
			0, "\r\n     123       \r   true",
			0, "33.t",
			1, "33 FALSE",
			1, "56FALSE",
			1, "4747falSe",
			1, "4747f",
			1, "4747 f",
			1, "47474.F",
			1, "47474. F",
			1, "47474 .F",
			
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
		int actual = limp.getAnswer(mockIncomingMessage(messageText));
		assertEquals(expected, actual);
	}
	
	public void testGetAnswer_badInput() {
		// when
		int answer = limp.getAnswer(mockIncomingMessage("something random"));
		
		// then
		assertEquals(-1, answer);
	}
	
	public void testIncorrectAnswerShouldNotGenerateReplyIfResponseNotSet() {
		// given
		when(properties.getIncorrectResponse()).thenReturn("");
		mockContact(contactDao, "+1234567890");
		
		// when
		limp.processMessage(mockIncomingMessage("+1234567890", "13f"));

		// then
		verify(frontlineController, never()).sendTextMessage(anyString(), anyString());
	}

	public void testIncorrectAnswerShouldGenerateReplyIfResponseSet() {
		// given
		when(properties.getIncorrectResponse()).thenReturn("TOTALLY WRONG");
		mockBinaryQuestion(13, true);
		mockContact(contactDao, "+1234567890");
		
		// when
		limp.processMessage(mockIncomingMessage("+1234567890", "13f"));

		// then
		verify(frontlineController).sendTextMessage("+1234567890", "TOTALLY WRONG");
	}
	
	public void testCorrectAnswerShouldNotGenerateReplyIfResponseNotSet() {
		// given
		when(properties.getCorrectResponse()).thenReturn("");
		mockBinaryQuestion(13, true);
		mockContact(contactDao, "+1234567890");
		
		// when
		limp.processMessage(mockIncomingMessage("+1234567890", "13t"));

		// then
		verify(frontlineController, never()).sendTextMessage(anyString(), anyString());
	}
	
	public void testCorrectAnswerShouldGenerateReplyIfResponseSet() {
		// given
		when(properties.getCorrectResponse()).thenReturn("well done");
		mockBinaryQuestion(13, true);
		mockContact(contactDao, "+1234567890");
		
		// when
		limp.processMessage(mockIncomingMessage("+1234567890", "13t"));

		// then
		verify(frontlineController).sendTextMessage("+1234567890", "well done");
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
	
	private void mockBinaryQuestion(int id, boolean correctAnswer) {
		Question q = mock(Question.class);
		when(q.getType()).thenReturn(Type.BINARY);
		when(q.getCorrectAnswer()).thenReturn(correctAnswer? 0: 1);
		
		AssessmentMessage m = mock(AssessmentMessage.class);
		when(m.getTopicItem()).thenReturn(q);
		
		when(assessmentMessageDao.get(id)).thenReturn(m);
	}
}
