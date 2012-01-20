package net.frontlinesms.plugins.learn.ui.assessment;

import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.ui.topic.TopicChoosingDialogHandlerTest;
import net.frontlinesms.test.spring.MockBean;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.*;

public class NewAssessmentDialogHandlerTest extends TopicChoosingDialogHandlerTest<NewAssessmentDialogHandler> {
	@MockBean private AssessmentDao assessmentDao;

	@Override
	protected NewAssessmentDialogHandler initHandler() {
		return new NewAssessmentDialogHandler(ui, assessmentDao, topicDao);
	}

	@Override
	protected void fillFieldsExceptTopic() {}
	
	@Override
	public void testTitle() {
		assertEquals("plugins.learn.assessment.new", $().getText());
	}

	@Override
	public void testSaveButton() {
		// given
		fillFieldsExceptTopic();
		$("cbTopic").setSelected("Music");
		
		// when
		$("btSave").click();
		
		// then
		verify(assessmentDao).save(assessmentWithTopic("Music"));
	}

}
