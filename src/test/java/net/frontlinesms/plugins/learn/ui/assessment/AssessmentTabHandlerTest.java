package net.frontlinesms.plugins.learn.ui.assessment;

import net.frontlinesms.plugins.learn.data.repository.*;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

public class AssessmentTabHandlerTest extends ThinletEventHandlerTest<AssessmentTabHandler> {
	@SuppressWarnings("unused")
	@MockBean private TopicDao topicDao;
	@SuppressWarnings("unused")
	@MockBean private AssessmentDao assessmentDao;

//> SETUP METHODS
	@Override
	protected AssessmentTabHandler initHandler() {
		return new AssessmentTabHandler(ui, ctx);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}
	
//> TEST METHODS
	public void testNewAssessmentButton() {		
		// when
		$("btNewAssessment").click();
		
		// then
		assertEquals("plugins.learn.assessment.new", $("dgEditAssessment").getText());
	}
}
