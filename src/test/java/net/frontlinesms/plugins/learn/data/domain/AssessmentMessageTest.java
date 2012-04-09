package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.junit.BaseTestCase;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.*;

public class AssessmentMessageTest extends BaseTestCase {
	/** instance under test */
	private AssessmentMessage m;
	
//> MOCKS
	private TopicItem t;
	
	@Override
	protected void setUp() throws Exception {
		t = mock(TopicItem.class);
		m = new AssessmentMessage(t);
	}
	
	public void testFrequencyInitialisesToOnce() {
		// expect
		assertEquals(Frequency.ONCE, m.getFrequency());
	}
	
	public void testStartDateIsEndDateForOnceOnlys() {
		// when
		m.setStartDate(TODAY);
		m.setEndDate(TOMORROW);
		m.setFrequency(Frequency.ONCE);
		
		// then
		assertEquals(TODAY.longValue(), m.getEndDate());
	}
	
	public void testGetMessageTextShouldReturnTopicItemTextWithIdSubstituted() {
		// givenTRply
		when(t.getMessageText()).thenReturn("Question: who is the fairest of them all?  Reply ${id}YOU or ${id}ME");
		
		// when
		String messageText = m.getMessageText();
		
		// then
		assertEquals("Question: who is the fairest of them all?  Reply 0YOU or 0ME", messageText);
	}
}
