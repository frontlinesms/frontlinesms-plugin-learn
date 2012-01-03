package net.frontlinesms.plugins.learn.ui;

import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletComponent;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

public class TopicTabHandlerTest extends ThinletEventHandlerTest<TopicTabHandler> {
	@MockBean private EventBus eventBus;
	@MockBean private TopicDao topicDao;

//> SETUP METHODS
	@Override
	protected TopicTabHandler initHandler() {
		return new TopicTabHandler(ui, ctx);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}
	
//> TEST METHODS
	public void testTopicsListVisible() {
		$("trTopics").exists();
	}
	
	@SuppressWarnings("unchecked")
	public void testTopicListUpdatesOnNewTopicAndDeletion() {
		// given
		Topic mockTopic1 = mock(Topic.class);
		Topic mockTopic2 = mock(Topic.class);
		when(topicDao.list()).thenReturn(asList(mockTopic1),
				asList(mockTopic1, mockTopic2),
				asList(mockTopic2),
				emptyList(Topic.class));
		
		// then on load there are no topics in the list
		assertEquals(0, $("trTopics").getChildCount());
		
		// when
		h.notify(new EntitySavedNotification<Topic>(mockTopic1));
		// then
		waitForUiEvents();
		assertEquals(1, $("trTopics").getChildCount());
		
		// when
		h.notify(new EntitySavedNotification<Topic>(mockTopic2));
		// then
		waitForUiEvents();
		assertEquals(2, $("trTopics").getChildCount());
		
		// when
		h.notify(new EntityDeletedNotification<Topic>(mockTopic1));
		// then
		waitForUiEvents();
		assertEquals(1, $("trTopics").getChildCount());
		
		// when
		h.notify(new EntityDeletedNotification<Topic>(mockTopic2));
		// then
		waitForUiEvents();
		assertEquals(0, $("trTopics").getChildCount());
	}

	public void testNewTopicButton() {
		// given
		ThinletComponent button = $("btNewTopic");
		
		// when
		button.click();
		
		// then
		assertEquals("i18n.plugins.learn.topic.new", $("dgEditTopic").getText());
	}
	
	public void testNewReinforcementButton() {
		// when
		$("btNewReinforcement").click();
		// then
		assertEquals("i18n.plugins.learn.reinforcement.new", $("dgEditReinforcement").getText());
	}
}