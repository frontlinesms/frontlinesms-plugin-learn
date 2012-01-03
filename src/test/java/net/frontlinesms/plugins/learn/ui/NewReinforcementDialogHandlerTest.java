package net.frontlinesms.plugins.learn.ui;

import java.util.ArrayList;

import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class NewReinforcementDialogHandlerTest extends ThinletEventHandlerTest<NewReinforcementDialogHandler> {
	@MockBean private ReinforcementDao dao;
	@MockBean private TopicDao topicDao;
	
//> SETUP METHODS
	@Override
	protected NewReinforcementDialogHandler initHandler() {
		return new NewReinforcementDialogHandler(ui, dao, topicDao);
	}

	@Override
	protected Object getRootComponent() {
		return h.getDialog();
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("i18n.plugins.learn.reinforcement.new", $().getText());
	}
	
	public void testTopicNotInitiallySet() {
		// when topic has not been set
		// then topic is "no topic set"
		assertEquals("i18n.plugins.learn.topic.choose", $("cbTopic").getText());
	}

	public void testSettingTopicExternally() {
		// given
		Topic[] t = mockTopics("Fascinating Topic 1", "Fascinating Topic 2");
		
		// when
		h.setTopic(t[0]);
		
		// then
		assertEquals("Fascinating Topic 1", $("cbTopic").getText());
	}
	
	public void testTopicNotEditable() {
		assertFalse($("cbTopic").isEditable());
	}
	
	public void testTopicValidation() {
		// given
		mockTopics("Psychology", "Physiognomy");
		$("taText").setText("Negative reinforcement is the best!");
		
		// when no topic is selected		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is selected and text is entered
		$("cbTopic").setSelected("Physiognomy");
		
		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
	
	public void testTextValidation() {
		// given
		mockTopics("Psychology", "Physiognomy");
		$("cbTopic").setSelected("Physiognomy");
		
		// when no text is entered		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when text is entered
		$("taText").setText("Negative reinforcement is the best!");

		// then save is enabled
		assertTrue($("btSave").isEnabled());
	}
	
	public void testCloseDialog() {
		// given the dialog is visible
		assertTrue($("dgEditReinforcement").isVisible());
		
		// when
		$("dgEditReinforcement").close();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
	}
	
	public void testCancelButton() {
		// given
		assertTrue($("dgEditReinforcement").isVisible());
		
		// when
		$("btCancel").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
	}
	
	public void testSaveButton() {
		// given
		mockTopics("Music");
		
		// when
		$("cbTopic").setSelected("Music");
		$("taText").setText("Remember: music can soothe, but it can also excite!");
		$("btSave").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
		verify(dao).save(reinforcementWithTextAndTopic(
				"Remember: music can soothe, but it can also excite!",
				"Music"));
	}
	
	public void testFailedSaveGivesWarning() {
		
	}
	
	private Topic[] mockTopics(String... names) {
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(String name : names) {
			Topic t = mock(Topic.class);
			when(t.getName()).thenReturn(name);
			when(topicDao.findByName(name)).thenReturn(t);
			topics.add(t);
		}
		when(topicDao.list()).thenReturn(topics);
		
		initUiForTests();
		
		return topics.toArray(new Topic[topics.size()]);
	}
}
