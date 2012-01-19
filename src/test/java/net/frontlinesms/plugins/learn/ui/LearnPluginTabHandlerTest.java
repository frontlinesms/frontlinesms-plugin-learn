package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

public class LearnPluginTabHandlerTest extends ThinletEventHandlerTest<LearnPluginTabHandler> {
	@SuppressWarnings("unused")
	@MockBean private TopicDao topicDao;
	@MockBean private EventBus eventBus;

//> SETUP METHODS
	@Override
	protected LearnPluginTabHandler initHandler() {
		LearnPluginTabHandler h = new LearnPluginTabHandler();
		h.init(ui, ctx);
		return h;
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}

//> TEST METHODS
	public void testManageTopicsLoaded() {
		$("tbManageTopics").exists();
	}
	
	public void testInit() {
		// given init was called in setup
		// then
		verify(eventBus).registerObserver(any(TopicTabHandler.class));
	}
}
