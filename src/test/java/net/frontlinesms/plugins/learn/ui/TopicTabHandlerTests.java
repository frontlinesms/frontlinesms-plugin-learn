package net.frontlinesms.plugins.learn.ui;

import static org.mockito.Mockito.mock;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.ui.ThinletComponent;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

public class TopicTabHandlerTests extends ThinletEventHandlerTest<TopicTabHandler> {
	private TopicDao topicDao;

//> SETUP METHODS
	@Override
	protected void setUp() throws Exception {
		topicDao = mock(TopicDao.class);
		super.setUp();
	}
	
	@Override
	protected TopicTabHandler initHandler() {
		return new TopicTabHandler(ui, topicDao);
	}
	
	@Override
	protected Object getRootComponent() {
		return h.getTab();
	}
	
//> TEST METHODS
	public void testTopicsListVisisble() {
		$("trTopics").exists();
	}
	
	public void testNewTopicButton() {
		// given
		ThinletComponent button = $("btNewTopic");
		
		// when
		button.click();
		
		// then
		$("dgNewTopic").exists();
	}
}