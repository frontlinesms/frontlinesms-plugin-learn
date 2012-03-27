package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.util.Calendar;

import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class NewAssessmentMessageDialogHandlerTest extends ThinletEventHandlerTest<NewAssessmentMessageDialogHandler> {
	private long TEST_DATE;
//	private AssessmentMessageDao dao;
	private TopicItem topicItem;
	private EditAssessmentMessageDialogOwner dialogOwner;
	
	@Override
	protected NewAssessmentMessageDialogHandler initHandler() {
		TEST_DATE = today9am();
		dialogOwner = mock(EditAssessmentMessageDialogOwner.class);
		topicItem = mock(TopicItem.class);
		when(topicItem.getMessageText()).thenReturn("Message text.");
		NewAssessmentMessageDialogHandler dialogHandler = new NewAssessmentMessageDialogHandler(ui, dialogOwner, topicItem);
		dialogHandler.setStartDate(TEST_DATE);
		return dialogHandler;
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}

//> TEST METHODS
	public void testDialogTitle() {
		assertEquals("plugins.learn.message.new", $().getText());
	}
	
	public void testTopicItemSummaryNotEditable() {
		assertFalse($("taSummary").isEditable());
	}
	
	public void testTopicItemSummaryDisplayed() {
		assertEquals("Message text.", $("taSummary").getText());
	}
	
	public void testRepeatInitialisedToOnce() {
		assertEquals("plugins.learn.frequency.once", $("cbRepeat").getText());
	}
	
	public void testStartDateInitialisedToNowishInLocalTimezone() {
		assertTimeEquals(TEST_DATE, "Start");
	}
	
	public void testDatePickerButtonAvailableForStartDate() {
		$("btShowStartDatePicker").exists();
	}
	
	public void testDatePickerLaunchForStartDate() {
		// when
		$("btShowStartDatePicker").click();
		
		// then
		assertTrue($("dateSelecter").isVisible());
	}
	
	public void testDatePickerDateUpdateForStartDate() {
		// given
		$("btShowStartDatePicker").click();
		assertTrue($("dateSelecter").isVisible());
		
		// when
		$("dateSelecter").find("pn1").getChild().withText("1").click();
		
		// then
		assertTimeEquals(firstOfTheMonth9am(), "Start");
	}
	
	public void testEndDateDisabled() {
		assertFalse($("pnEndDate").isEnabled());
	}

	
	public void testRepeatNotEditableByHand() {
		assertFalse($("cbRepeat").isEditable());
	}
	
	public void testRepeatOptions() {
		assertEquals("Repeat options should be as listed",
				i18n("plugins.learn.frequency.", "once", "daily", "weekly", "monthly"),
				$("cbRepeat").getOptions());
	}
	
	public void testCloseDialog() {
		// given the dialog is visible
		assertTrue($().isVisible());
		
		// when
		$().close();
		
		// then
		assertFalse($().isVisible());
	}
	
	public void testCancelButton() {
		// given
		assertTrue($().isVisible());
		
		// when
		$("btCancel").click();
		
		// then
		assertFalse($().isVisible());
	}
	
	public void testSaveButton() {
		// when
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dialogOwner).notifyAssessmentMessageSaved(assessmentMessageWithTopicItemAndStartDateAndRepeat(topicItem,
				TEST_DATE, Frequency.ONCE));
	}
	
//> TEST HELPER METHODS
	private String[] i18n(String root, String... labels) {
		for (int i = 0; i < labels.length; i++) {
			labels[i] = root + labels[i];
		}
		return labels;
	}
	
	private void assertTimeEquals(long expectedTime, String fieldName) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(expectedTime);
		assertEquals(InternationalisationUtils.formatDate(expectedTime), $("tf" + fieldName + "Date").getText());
		assertEquals(InternationalisationUtils.formatTime(expectedTime), $("tf" + fieldName + "Time").getText());
	}
}
