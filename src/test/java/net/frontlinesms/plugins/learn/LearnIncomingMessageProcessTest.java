package net.frontlinesms.plugins.learn;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;

import static org.mockito.Mockito.*;

public class LearnIncomingMessageProcessTest extends BaseTestCase {
	/** instance under test */
	private LearnIncomingMessageProcessor limp;
	
	@Override
	protected void setUp() throws Exception {
		limp = new LearnIncomingMessageProcessor();
	}
	
//> TESTS
	public void testGetAssessmentMessage() {
		mockAssessmentMessageDao();
		final Object[] PAIRS = {
				33, "33 TRUE",
				56, "56TRUE",
				4747, "4747trUe",
				33, "33 FALSE",
				56, "56FALSE",
				4747, "4747falSe",
				
				23, "23 A",
				44, "44 b",
				69, "69C",
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
	
	public void testGetAnswer() {
		final Object[] PAIRS = {
			0, "33 TRUE",
			0, "56TRUE",
			0, "4747trUe",
			1, "33 FALSE",
			1, "56FALSE",
			1, "4747falSe",
			
			0, "23 A",
			1, "44 b",
			2, "69C",
		};
		for (int i = 0; i < PAIRS.length; i+=2) {
			testGetAnswer((Integer) PAIRS[i], (String) PAIRS[i+1]);
		}
	}
	
	private void testGetAnswer(int expected, String messageText) {
		int actual = limp.getAnswer(mockMessage(messageText));
		assertEquals(expected, actual);
	}
	
//> SETUP
	private FrontlineMessage mockMessage(String messageText) {
		FrontlineMessage m = mock(FrontlineMessage.class);
		when(m.getTextContent()).thenReturn(messageText);
		return m;
	}
	
	private void mockAssessmentMessageDao() {
		AssessmentMessageDao dao = mock(AssessmentMessageDao.class);
		when(dao.get(anyLong())).thenAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				long id = (Long) invocation.getArguments()[0];
				AssessmentMessage m = mock(AssessmentMessage.class);
				when(m.getId()).thenReturn(id);
				return m;
			}
		});
		inject(limp, "assessmentMessageDao", dao);
	}
}
