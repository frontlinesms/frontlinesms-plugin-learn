package net.frontlinesms.plugins.learn.ui;

import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;

public class LearnPluginTabHandlerTests extends ThinletEventHandlerTest<LearnPluginTabHandler> {
	private TopicDao topicDao;

//> SETUP METHODS
	@Override
	protected void setUp() throws Exception {
		topicDao = mock(TopicDao.class);
		super.setUp();
	}
	
	@Override
	protected LearnPluginTabHandler initHandler() {
		LearnPluginTabHandler h = new LearnPluginTabHandler();
		h.setTopicDao(topicDao);
		h.init(ui);
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
}
