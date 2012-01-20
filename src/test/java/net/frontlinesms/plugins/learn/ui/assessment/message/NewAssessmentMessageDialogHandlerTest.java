package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

public class NewAssessmentMessageDialogHandlerTest extends ThinletEventHandlerTest<NewAssessmentMessageDialogHandler> {
	private TopicItem topicItem;
	
	@Override
	protected NewAssessmentMessageDialogHandler initHandler() {
		topicItem = mock(TopicItem.class);
		when(topicItem.getMessageText()).thenReturn("Message text.");
		return new NewAssessmentMessageDialogHandler(ui, topicItem);
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}

//> TEST METHODS
	public void testTopicItemSummaryDisplayed() {
		assertEquals("Message text.", $("tfSummary").getText());
	}
	
	public void testRepeatInitialisedToOnce() {
		TODO("Get the repeats widget and check the value is once");
	}
	
	public void testStartDateInitialisedToNowish() {
		TODO("implement");
	}
	
	public void testEndDateDisabled() {
		TODO("implement");
	}
	
	public void testRepeatOptions() {
		TODO("implement to check all repeat options are available - once, daily, weekly, monthly");
	}
	
	public void testCancel() {
		TODO("implement");
	}
	
	public void testClose() {
		TODO("implement");
	}
	
	public void testSave() {
		TODO("implement");
		
		TODO("check DAO was called as expected");
		
		TODO("check callback is made to the dialog owner (normally dgEditAssessment, but in this case a little thing just for the unit test.");
	}
}
