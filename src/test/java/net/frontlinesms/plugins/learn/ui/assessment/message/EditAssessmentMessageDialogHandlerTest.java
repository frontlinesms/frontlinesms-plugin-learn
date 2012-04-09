package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.data.domain.Frequency.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class EditAssessmentMessageDialogHandlerTest extends ThinletEventHandlerTest<EditAssessmentMessageDialogHandler> {
	private TopicItem topicItem;
	private EditAssessmentMessageDialogOwner dialogOwner;
	
	@Override
	protected EditAssessmentMessageDialogHandler initHandler() {
		dialogOwner = mock(EditAssessmentMessageDialogOwner.class);
		topicItem = mock(TopicItem.class);
		when(topicItem.getMessageText()).thenReturn("Message text.");
		AssessmentMessage m = new AssessmentMessage(topicItem);
		m.setFrequency(WEEKLY);
		m.setStartDate(parseDate("24/12/2011 09:00"));
		m.setEndDate(parseDate("25/12/2011"));
		EditAssessmentMessageDialogHandler dialogHandler = new EditAssessmentMessageDialogHandler(ui, dialogOwner, m);
		return dialogHandler;
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}

//> TEST METHODS
	public void testDialogTitle() {
		assertEquals("plugins.learn.message.edit", $().getText());
	}
	
	public void testTopicItemSummaryNotEditable() {
		assertFalse($("taSummary").isEditable());
	}
	
	public void testTopicItemSummaryDisplayed() {
		assertEquals("Message text.", $("taSummary").getText());
	}
	
	public void testRepeatInitialisedToWeekly() {
		waitForUiEvents();
		assertEquals(WEEKLY.getI18nKey(), $("cbRepeat").getText());
	}
	
	public void testStartDateInitialisedToLastChristmasEve() {
		assertEquals("24/12/2011", $("tfStartDate").getText());
		assertEquals("09:00", $("tfStartTime").getText());
	}
	
	public void testEndDateInitialisedToLastChristmasDay() {
		assertEquals("25/12/2011", $("tfEndDate").getText());
	}
}
