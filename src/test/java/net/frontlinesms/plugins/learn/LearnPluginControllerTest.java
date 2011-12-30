package net.frontlinesms.plugins.learn;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.ui.UiGeneratorController;

import static org.mockito.Mockito.*;

public class LearnPluginControllerTest extends BasePluginControllerTests<LearnPluginController> {
	@MockBean private EventBus eventBus;
	@MockBean private TopicDao topicDao;
	
	public Class<LearnPluginController> getControllerClass() {
		return LearnPluginController.class;
	}
	
	public void testTabAvailable() {
		UiGeneratorController ui = mock(UiGeneratorController.class);
		inject(controller, "ctx", ctx);
		assertNotNull(controller.getTab(ui));
	}
}

