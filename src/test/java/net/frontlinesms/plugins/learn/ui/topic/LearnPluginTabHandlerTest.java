package net.frontlinesms.plugins.learn.ui.topic;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.ui.assessment.AssessmentTabHandler;
import net.frontlinesms.plugins.learn.ui.topic.LearnPluginTabHandler;
import net.frontlinesms.plugins.learn.ui.topic.TopicTabHandler;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.test.ui.ThinletEventHandlerTest;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

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
	public void testManageTopicsTabLoaded() {
		$("tbManageTopics").exists();
	}
	
	public void testAssessmentsTabLoaded() {
		$("tbAssessments").exists();
	}
	
	public void testGradebookTabLoaded() {
		$("tbGradebook").exists();
	}
	
	public void testInit() {
		// given init was called in setup
		// then
		verify(eventBus, times(2)).registerObserver(eventObserversOfClass(TopicTabHandler.class, AssessmentTabHandler.class));
	}
}
