package net.frontlinesms.plugins.learn.data.domain;

import java.util.List;

import net.frontlinesms.junit.BaseTestCase;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class AssessmentTest extends BaseTestCase {
	public void testSettingMessagesUpdatesStartAndEndDate() {
		// given

		AssessmentMessage m1 = mockAssessmentMessage(TODAY, TOMORROW);
		AssessmentMessage m2 = mockAssessmentMessage(YESTERDAY, TODAY);
		final List<AssessmentMessage> messages = asList(m1, m2);
		
		Assessment a = new Assessment();
		assertNull(a.getStartDate());
		assertNull(a.getEndDate());
		
		// when
		a.setMessages(messages);
		
		// then
		assertEquals(YESTERDAY, a.getStartDate());
		assertEquals(TOMORROW, a.getEndDate());
	}
	
	public void testGetMessagesIsNeverNull() {
		// given
		Assessment a = new Assessment();
		
		// then
		assertNotNull(a.getMessages());
	}
	
	public void testGetStartDateNullWhenNoMessages() {
		// given
		Assessment a = new Assessment();
		
		// then
		assertNull(a.getStartDate());
	}
	
	public void testGetEndDateNullWhenNoMessages() {
		// given
		Assessment a = new Assessment();
		
		// then
		assertNull(a.getEndDate());
	}
}
