package net.frontlinesms.plugins.learn.ui.assessment.message;

import java.util.Calendar;

import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static net.frontlinesms.plugins.learn.data.domain.Frequency.*;

public class NewAssessmentMessageDialogHandlerTest extends ThinletEventHandlerTest<NewAssessmentMessageDialogHandler> {
	private long TEST_DATE;
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
		dialogHandler.setEndDate(TEST_DATE);
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
	
	public void testEndDateInitialisedToNowishInLocalTimezone() {
		assertTimeEquals(TEST_DATE, "End");
	}
	
	public void testDatePickerButtonAvailableForStartDate() {
		$("btShowStartDatePicker").exists();
	}
	
	public void testDatePickerButtonAvailableForEndDate() {
		$("btShowEndDatePicker").exists();
	}
	
	public void testEndDateDoesNotHaveTimeField() {
		// expect
		assertFalse($("tfEndTime").isVisible());
	}
	
	public void testDatePickerLaunchForStartDate() {
		// when
		$("btShowStartDatePicker").click();
		
		// then
		assertTrue($("dateSelecter").isVisible());
	}
	
	public void testDatePickerLaunchForEndDate() {
		// when
		$("cbRepeat").setSelected(WEEKLY);
		$("btShowEndDatePicker").click();
		
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
	
	public void testDatePickerDateUpdateForEndDate() {
		// given
		$("cbRepeat").setSelected(WEEKLY);
		$("btShowEndDatePicker").click();
		assertTrue($("dateSelecter").isVisible());
		
		// when
		$("dateSelecter").find("pn1").getChild().withText("1").click();
		
		// then
		assertTimeEquals(firstOfTheMonth9am(), "End");
	}
	
	public void testEndDateDisabled() {
		assertFalse($("pnEndDate").isEnabled());
	}

	public void settingRepeatToMoreOftenThanOnceShouldEnableEndDate() {
		// when
		$("cbRepeat").setSelected(WEEKLY);
		
		// then
		assertFalse($("pnEndDate").isEnabled());
	}
	
	public void resettingRepeatToOnceShouldDisableEndDate() {
		// given
		$("cbRepeat").setSelected(WEEKLY);
		
		// when
		$("cbRepeat").setSelected(ONCE);
		
		// then
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
	
	public void testSaveButtonEnabledWithNoEndDateIfFrequencyIsOnce() {
		// expect
		assertTrue($("btSave").isEnabled());
	}
	
	public void testSaveButtonDisabledWithNoEndDateIfFrequencyIsNotOnce() {
		// given
		$("tfEndDate").setText("");
		for(Frequency f : array(DAILY, WEEKLY, MONTHLY)) {
			// when
			setFrequency(f);
			
			// then
			assertFalse($("btSave").isEnabled());
		}
	}
	
	public void testSaveButtonReenabledWhenFrequencyResetToOnceWithNoEndDate() {
		// given
		$("tfEndDate").setText("");
		setFrequency(DAILY);
		assertFalse($("btSave").isEnabled());
		
		// when
		setFrequency(ONCE);
		
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testSaveButtonEnabledForAllFrequenciesIfEndDateSet() {
		for(Frequency f : Frequency.values()) {
			// when
			setFrequency(f);
			
			// then
			assertTrue($("btSave").isEnabled());
		}
	}
	
	public void testSaveButtoneTogglesWithEndDateIfFrequencyNotOnce() {

		for(Frequency f : array(DAILY, WEEKLY, MONTHLY)) {
			// given
			$("tfEndDate").setText("");
			$("cbRepeat").setSelected(f);
			assertFalse($("btSave").isEnabled());
			
			// when
			$("tfEndDate").setText("25/12/2012");
			
			// then
			assertTrue($("btSave").isEnabled());
			
			// when
			$("tfEndDate").setText("");
			
			// then
			assertFalse($("btSave").isEnabled());
		}
	}
	
	public void testSaveButtonDisabledIfStartDateNotSet() {
		// when
		$("tfStartDate").setText("");
		
		// then
		assertFalse($("btSave").isEnabled());
	}
	
	public void testSaveButtonDisabledIfStartTimeNotSet() {
		// when
		$("tfStartTime").setText("");
		
		// then
		assertFalse($("btSave").isEnabled());
	}
	
	public void testSaveButton() {
		// when
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dialogOwner).notifyAssessmentMessageSaved(assessmentMessageWithTopicItemAndStartDateAndRepeat(topicItem,
				TEST_DATE, Frequency.ONCE));
	}
	
	public void testSaveButtonWithChangedStartDate() {
		// given
		setFrequency(MONTHLY);
		$("tfStartDate").setText("1/1/2012");
		$("tfStartTime").setText("13:00");
		$("tfEndDate").setText("30/6/2012");
		
		// when
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dialogOwner).notifyAssessmentMessageSaved(assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(topicItem,
				"1/1/2012 13:00", MONTHLY, "30/6/2012 13:00"));
	}
	
	public void testSaveButtonWithChangedFrequency() {
		// given
		setFrequency(MONTHLY);
		$("tfEndDate").setText("25/12/2012");
		
		// when
		$("btSave").click();
		
		// then
		assertFalse($().isVisible());
		verify(dialogOwner).notifyAssessmentMessageSaved(assessmentMessageWithTopicItemAndStartDateAndRepeatAndEndDate(topicItem,
				TEST_DATE, Frequency.MONTHLY, "25/12/2012 09:00"));
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
		if(fieldName == "Start") {
			assertEquals(InternationalisationUtils.formatTime(expectedTime), $("tf" + fieldName + "Time").getText());
		}
	}
	
	private void setFrequency(Frequency f) {
		$("cbRepeat").setSelected(f.getI18nKey());
	}
}
