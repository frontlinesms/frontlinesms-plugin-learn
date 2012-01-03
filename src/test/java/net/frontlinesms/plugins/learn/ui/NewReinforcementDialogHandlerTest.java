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
		return null;
	}

	@Override
	protected Object getRootComponent() {
		return null;
	}
	
//> TEST METHODS
	public void testTitle() {
		assertEquals("i18n.plugins.learn.reinforcement.new", $().getText());
	}
	
	public void testUnsetTopic() {
		// when topic has not been set
		// then topic is "no topic set"
		assertNull($("cbTopic").getSelected().getAttachment());
		assertEquals("i18n.plugins.learn.topic.choose", $("cbTopic").getSelected().getText());
	}

	public void testSettingTopicExternally() {
		// given
		Topic[] t = mockTopics("Fascinating Topic 1", "Fascinating Topic 2");
		
		// when
		h.setTopic(t[0]);
		
		// then
		assertEquals(t[0], $("cbTopic").getSelected().getAttachment());
		assertEquals("Fascinating Topic 1", $("cbTopic").getSelected().getText());
	}
	
	public void testValidation() {
		// given
		mockTopics("Psychology", "Physiognomy");
		
		// when no topic is selected and no text is entered		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is entered, but no text is written
		$("cbTopic").setSelectedByText("Physiognomy");
		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when text is written but no topic is selected
		$("cbTopic").setSelected(null);
		$("taText").setText("Negative reinforcement is the best!");
		
		// then save is disabled
		assertFalse($("btSave").isEnabled());
		
		// when topic is selected and text is entered
		$("cbTopic").setSelectedByText("Physiognomy");
		
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
		$("btClose").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
	}
	
	public void testSaveButton() {
		// given
		mockTopics("Music");
		
		// when
		$("cbTopic").setSelectedByText("Music");
		$("taText").setText("Remember: music can soothe, but it can also excite!");
		$("btSave").click();
		
		// then
		assertFalse($("dgEditReinforcement").isVisible());
		verify(dao).save(reinforcementWithTextAndTopic(
				"Remember: music can soothe, but it can also excite!",
				"Music"));
	}
	
	private Topic[] mockTopics(String... names) {
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(String name : names) {
			Topic t = mock(Topic.class);
			when(t.getName()).thenReturn(name);
			topics.add(t);
		}
		when(topicDao.list()).thenReturn(topics);
		return topics.toArray(new Topic[topics.size()]);
	}
}
