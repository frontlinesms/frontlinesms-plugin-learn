package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.junit.BaseTestCase;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class AssessmentMessageTest extends BaseTestCase {
	public void testFrequencyInitialisesToOnce() {
		// when
		AssessmentMessage m = new AssessmentMessage();
		
		// then
		assertEquals(Frequency.ONCE, m.getFrequency());
	}
	
	public void testStartDateIsEndDateForOnceOnlys() {
		// given
		AssessmentMessage m = new AssessmentMessage();
		
		// when
		m.setStartDate(TODAY);
		m.setEndDate(TOMORROW);
		m.setFrequency(Frequency.ONCE);
		
		// then
		assertEquals(TODAY.longValue(), m.getEndDate());
	}
}
