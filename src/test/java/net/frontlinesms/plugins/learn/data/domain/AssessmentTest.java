package net.frontlinesms.plugins.learn.data.domain;

import java.util.List;

import net.frontlinesms.junit.BaseTestCase;

import static java.util.Arrays.asList;

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
}
