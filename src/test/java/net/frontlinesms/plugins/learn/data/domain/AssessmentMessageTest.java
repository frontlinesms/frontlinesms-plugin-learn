package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.junit.BaseTestCase;

public class AssessmentMessageTest extends BaseTestCase {
	public void testFrequencyInitialisesToOnce() {
		// when
		AssessmentMessage m = new AssessmentMessage();
		
		// then
		assertEquals(Frequency.ONCE, m.getFrequency());
	}
}
