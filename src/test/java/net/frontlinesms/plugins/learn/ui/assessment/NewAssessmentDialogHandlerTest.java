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
	
	public void testGroupSelecterInitialisedEmpty() {
		TODO("Test that the group selecter is initially empty");
	}
	
	public void testMessagesInitialisedEmpty() {
		assertEquals(0, $("lsMessages").getChildCount());
	}
	
	public void testGroupInitialisation() {
		// given
		TODO("fill all fields except group chooser");
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		TODO("set a valid group");
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testMessageValidation() {
		// given
		TODO("fill all fields except messages");
		
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		TODO("add a valid message");
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testNewMessageButton() {
		// when
		$("btNewMessage").click();
		
		// then
		assertEquals("plugins.learn.assessment.message.new", $("dgEditAssessmentMessage").getText());
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
