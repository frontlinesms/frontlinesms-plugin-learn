package net.frontlinesms.plugins.learn.ui.assessment;

import java.util.List;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.TopicItemDao;
import net.frontlinesms.plugins.learn.ui.topic.TopicChoosingDialogHandlerTest;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static net.frontlinesms.plugins.learn.data.domain.Frequency.*;

public class EditAssessmentDialogHandlerTest extends TopicChoosingDialogHandlerTest<EditAssessmentDialogHandler> {
//> STATIC TEST DATA
	private static final String[][] MESSAGE_TABLE_INITIAL_CONTENT = { 
			array("mock topic item: 0", "25/12/2011", ONCE.getI18nKey(), ""),
			array("mock topic item: 1"),
			array("mock topic item: 2", "1/1/2012", DAILY.getI18nKey(), "8/1/2012"),
			array("mock topic item: 3"),
			array("mock topic item: 4", "2/1/2012", WEEKLY.getI18nKey(), "30/1/2012")};
	
//> MOCKS
	@MockBean private AssessmentDao dao;
	@MockBean private GroupDao groupDao;
	@MockBean private TopicItemDao topicItemDao;
	private Assessment a;
	private Group g;
	private Topic t;
	private List<AssessmentMessage> assessmentMessages;
	private List<TopicItem> topicItems;
	
//> SETUP METHODS
	@Override
	protected void setUp() throws Exception {
		a = new Assessment();
		
		t = mockTopic("the topic");
		a.setTopic(t);

		g = mockGroup("the group");
		a.setGroup(g);
		
		TopicItem[] ti = mockTopicItems(5);
		topicItems = asList(ti);

		assessmentMessages = asList(
				mockMessageWithTopicItem(ti[0], Frequency.ONCE, "25/12/2011", null),
				mockMessageWithTopicItem(ti[2], Frequency.DAILY, "1/1/2012", "8/1/2012"),
				mockMessageWithTopicItem(ti[4], Frequency.WEEKLY, "2/1/2012", "30/1/2012"));
		a.setMessages(assessmentMessages);
		
		super.setUp();
		
		when(topicItemDao.getAllByTopic(t)).thenReturn(topicItems);
		initUiForTests();
	}

	@Override
	protected void fillFieldsExceptTopic() {
	}

	@Override
	protected EditAssessmentDialogHandler initHandler() {
		return new EditAssessmentDialogHandler(ui, dao, groupDao, topicDao, topicItemDao, a);
	}
	
//> TEST METHODS
	@Override
	public void testTitle() {
		assertEquals("plugins.learn.assessment.edit", $().getText());
	}
	
	public void testSaveButtonEnabledForEdit() {
		assertTrue($("btSave").isEnabled());
	}

	@Override
	public void testSaveButton() {
		// when
		$("btSave").click();
		
		// then
		verify(dao).save(a);
		assertFalse($().isVisible());
	}
	
	public void testTopicInitialisation() {
		assertEquals("the group", $("tfGroup").getText());
	}
	
	public void testGroupInitialisation() {
		assertEquals("the topic", $("cbTopic").getText());
	}
	
	public void testMessageTableHeader() {
		// expect
		assertEquals("Assessment message table headers.",
				array("plugins.learn.message.summary",
						"plugins.learn.message.date.start",
						"plugins.learn.message.repeat",
						"plugins.learn.message.date.end"),
				$("tbMessages").getColumnTitles());
	}
	
	public void testMessageTableContentInitialisation() {
		// expect
		for(int i=0; i<5; ++i) {
			assertRowContentAsInitialised(i);
		}
	}

	public void testTableUpdatesCorrectlyAfterAssessmentMessageUpdate() {
		// given
		AssessmentMessage m = assessmentMessages.get(0);
		when(m.getFrequency()).thenReturn(MONTHLY);
		when(m.getEndDate()).thenReturn(parseDate("25/12/2012"));
		
		// when
		h.notifyAssessmentMessageSaved(m);
		
		// then
		assertEquals("Row 0",
				array("mock topic item: 0", "25/12/2011", MONTHLY.getI18nKey(), "25/12/2012"),
				$("tbMessages").getRowText(0));
		for(int i=1; i<5; ++i) {
			assertRowContentAsInitialised(i);
		}
	}
	
	public void testTableUpdatesCorrectlyAfterAssessmentMessageSave() {
		// given
		AssessmentMessage m = mockMessageWithTopicItem(topicItems.get(1), ONCE, "3/10/2012", null);
		
		// when
		h.notifyAssessmentMessageSaved(m);
		
		// then
		assertEquals("Row 1",
				array("mock topic item: 1", "3/10/2012", ONCE.getI18nKey(), ""),
				$("tbMessages").getRowText(1));

		assertRowContentAsInitialised(0);
		for(int i=2; i<5; ++i) {
			assertRowContentAsInitialised(i);
		}
	}
	
	public void testMessageTableAttachmentInitialisation() {
		ThinletComponent[] rows = $("tbMessages").getRows();
		
		assertEquals(topicItems.size(), rows.length);
		
		for(ThinletComponent c : rows) {
			Object attachment = c.getAttachment();
			if(attachment instanceof AssessmentMessage) {
				TopicItem t = ((AssessmentMessage) attachment).getTopicItem();
				if(!assessmentMessages.remove(attachment) ||
						!topicItems.remove(t)) {
					fail("Unexpected assessment message!");
				}
			} else if(attachment instanceof TopicItem) {
				if(!topicItems.remove(attachment)) {
					fail("Unexpected topic item!");
				}
			} else fail("Unexpected attachment: " + attachment);
		}

		assertEquals(0, topicItems.size());
		assertEquals(0, assessmentMessages.size());
	}
	
	public void testDoubleClickingTopicItemTriggersNewMessageWindow() {
		// when
		$("tbMessages").getRow(1).doubleClick();
		
		// then
		assertEquals("plugins.learn.message.new", $("dgEditAssessmentMessage").getText());
	}
	
	public void testDoubleClickingAssessmentMessageTriggersEditMessageWindow() {
		// when
		$("tbMessages").getRow(0).doubleClick();
		
		// then
		assertEquals("plugins.learn.message.edit", $("dgEditAssessmentMessage").getText());
	}
	
//> ASSERT METHODS
	private void assertRowContentAsInitialised(int i) {
		assertEquals("Row " + i,
				MESSAGE_TABLE_INITIAL_CONTENT[i],
				$("tbMessages").getRowText(i));
	}
}
