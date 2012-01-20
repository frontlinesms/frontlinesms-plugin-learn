package net.frontlinesms.plugins.learn.ui.assessment;

import java.util.List;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.TopicItemDao;
import net.frontlinesms.plugins.learn.ui.topic.NewTopicChoosingDialogHandlerTest;
import net.frontlinesms.test.spring.MockBean;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

public class NewAssessmentDialogHandlerTest extends NewTopicChoosingDialogHandlerTest<NewAssessmentDialogHandler> {
	@MockBean private AssessmentDao assessmentDao;
	@MockBean private GroupDao groupDao;
	@MockBean private TopicItemDao topicItemDao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		when(topicItemDao.getAllByTopic(any(Topic.class)))
				.thenReturn(asList(
						fakeTopicItem("Item 1"), fakeTopicItem("Item 2")));
		
		List<Group> groups = asList(mockGroup("Jets"), mockGroup("Sharks"));
		when(groupDao.getChildGroups(null)).thenReturn(groups);
	}
	
	@Override
	protected NewAssessmentDialogHandler initHandler() {
		return new NewAssessmentDialogHandler(ui, assessmentDao, groupDao, topicDao, topicItemDao);
	}
	
	@Override
	protected String[] getAllFieldNames() {
		return new String[] {"cbTopic", "tbMessages", "tfGroup"};
	}
	
	@Override
	protected void setValidValue(String fieldName) {
		if(fieldName.equals("cbTopic")) {
			$("cbTopic").setSelected("Music");
		} else if(fieldName.equals("tbMessages")) {
			// this will only work if cbTopic is already selected...
			$("tbMessages").getChild(0).setAttachment(mock(AssessmentMessage.class));
			h.validate(null);
		} else if(fieldName.equals("tfGroup")) {
			h.groupSelectionCompleted(mockGroup("Beach Boys"));
			h.validate(null);
		} else throw new IllegalArgumentException();
	}
	
	@Override
	public void testTitle() {
		assertEquals("plugins.learn.assessment.new", $().getText());
	}
	
	public void testGroupSelecterInitialisedEmptyAndNotEditable() {
		assertEquals("", $("tfGroup").getText());
		assertFalse($("tfGroup").isEditable());
	}
	
	public void testMessagesInitialisedEmpty() {
		assertEquals(0, $("tbMessages").getChildCount());
	}
	
	public void testGroupSelecterTriggerButton() {
		// when
		$("btSelectGroup").click();
		// then
		$("dgGroupSelecter").isVisible();
	}
	
	public void testMessagesUpdatedWhenTopicChanged() {
		// when
		$("cbTopic").setSelected("Music");
		
		// then
		assertEquals(2, $("tbMessages").getChildCount());
	}
	
	public void testDoubleClickingOnMessageTriggersMessageEditDialog() {
		// given
		$("cbTopic").setSelected("Music");
		assertEquals(2, $("tbMessages").getChildCount());
		
		// when
		$("tbMessages").getChild(0).doubleClick();
		
		// then
		assertTrue($("dgEditAssessmentMessage").isVisible());
	}
	
	public void testGroupInitialisation() {
		// given
		setValidValuesExcept("tfGroup");
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		setValidValue("tfGroup");
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	public void testMessageValidation() {
		// given
		setValidValuesExcept("tbMessages");
		
		// then
		assertFalse($("btSave").isEnabled());
		
		// when
		setValidValue("tbMessages");
		// then
		assertTrue($("btSave").isEnabled());
	}
	
	@Override
	public void testSaveButton() {
		// given
		setAllFieldsValid();
		
		// when
		$("btSave").click();
		
		// then
		verify(assessmentDao).save(assessmentWithTopic("Music"));
		TODO("Add check for group and assessments to verification.");
	}
	
	@Override
	public void testTopicValidation() {
		// given
		setValidValuesExcept("cbTopic", "tbMessages");
		
		// when no topic is selected		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is selected and text is entered
		$("cbTopic").setSelected("Psychology");
		setValidValue("tbMessages");
		
		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}

	private TopicItem fakeTopicItem(String messageText) {
		TopicItem topicItem = new TopicItem() {};
		topicItem.setMessageText(messageText);
		return topicItem;
	}
	
	private Group mockGroup(String name) {
		Group g = mock(Group.class);
		when(g.getName()).thenReturn(name);
		return g;
	}
}
