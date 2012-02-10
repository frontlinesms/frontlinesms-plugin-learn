package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.junit.BaseTestCase;

import static org.mockito.Mockito.*;

public class AssessmentMessageResponseTest extends BaseTestCase {
	/** instance under test */
	private AssessmentMessageResponse r;
	
	@Override
	protected void setUp() throws Exception {
		r = new AssessmentMessageResponse();
	}
	
	public void testStudentAccessors() {
		// given
		Contact c = mock(Contact.class);
		
		// when
		r.setStudent(c);
		
		// then
		assertEquals(c, r.getStudent());
	}
	
	public void testCorrectnessAccessors() {
		assertFalse(r.isCorrect());
		// when
		r.setCorrect(true);
		// then
		assertTrue(r.isCorrect());
	}
	
	public void testAssessmentMessageAccessors() {
		// setup
		AssessmentMessage m = mock(AssessmentMessage.class);
		
		// when
		r.setAssessmentMessage(m);
		
		// then
		assertEquals(m, r.getAssessmentMessage());
	}
	
	public void testAnswerAccessors() {
		// setup
		assertEquals(0, r.getAnswer());
		
		// when
		r.setAnswer(2);
		
		// then
		assertEquals(2, r.getAnswer());
	}
}
