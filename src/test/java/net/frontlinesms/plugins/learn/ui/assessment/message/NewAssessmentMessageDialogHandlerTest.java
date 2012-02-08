package net.frontlinesms.plugins.learn.ui.assessment.message;

import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class NewAssessmentMessageDialogHandlerTest extends ThinletEventHandlerTest<NewAssessmentMessageDialogHandler> {
	private long TEST_DATE;
	private AssessmentMessageDao dao;
	private TopicItem topicItem;
	private EditAssessmentMessageDialogOwner dialogOwner;
	
	@Override
	protected NewAssessmentMessageDialogHandler initHandler() {
		TEST_DATE = today9am();
		dao = mock(AssessmentMessageDao.class);
		dialogOwner = mock(EditAssessmentMessageDialogOwner.class);
		topicItem = mock(TopicItem.class);
		when(topicItem.getMessageText()).thenReturn("Message text.");
		NewAssessmentMessageDialogHandler dialogHandler = new NewAssessmentMessageDialogHandler(ui, dialogOwner, dao, topicItem);
		dialogHandler.setStartDate(TEST_DATE);
		return dialogHandler;
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}

//> TEST METHODS
	public void testDialogTitle() {
		assertEquals("plugins.learn.assessment.message.new", $().getText());
	}
	
	public void testTopicItemSummaryNotEditable() {
		assertFalse($("taSummary").isEditable());
	}
	
	public void testTopicItemSummaryDisplayed() {
		assertEquals("Message text.", $("taSummary").getText());
	}
	
	public void testRepeatInitialisedToOnce() {
		assertEquals("plugins.learn.repeat.once", $("cbRepeat").getText());
	}
	
	public void testStartDateInitialisedToNowish() {
		assertEquals(TEST_DATE, h.getStartDate());
	}
	
	public void testEndDateDisabled() {
		assertFalse($("pnEndDate").isEnabled());
	}

	
	public void testRepeatNotEditableByHand() {
		assertFalse($("cbRepeat").isEditable());
	}
	
	public void testRepeatOptions() {
		assertEquals("Repeat options should be as listed",
				i18n("plugins.learn.repeat.", "once", "daily", "weekly", "monthly"),
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
		verify(dao).save(assessmentMessageWithTopicItemAndStartDateAndRepeat(topicItem,
				TEST_DATE, Frequency.ONCE));
		assertFalse($().isVisible());
		verify(dialogOwner).notifyAssessmentMessageSaved(any(AssessmentMessage.class));
		
		TODO("check callback is made to the dialog owner (normally dgEditAssessment, but in this case a little thing just for the unit test.");
	}
	
//> TEST HELPER METHODS
	private String[] i18n(String root, String... labels) {
		for (int i = 0; i < labels.length; i++) {
			labels[i] = root + labels[i];
		}
		return labels;
	}
}
