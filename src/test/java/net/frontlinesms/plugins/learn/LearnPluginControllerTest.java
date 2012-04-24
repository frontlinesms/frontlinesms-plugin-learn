package net.frontlinesms.plugins.learn;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.test.spring.MockBean;
import net.frontlinesms.ui.UiGeneratorController;

import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
public class LearnPluginControllerTest extends BasePluginControllerTests<LearnPluginController> {
	@MockBean private EventBus eventBus;
	@MockBean private TopicDao topicDao;
	@MockBean private AssessmentDao assessmentDao;
	@MockBean private LearnIncomingMessageProcessor learnIncomingMessageProcessor;
	@MockBean private Scheduler scheduler;
	
	public Class<LearnPluginController> getControllerClass() {
		return LearnPluginController.class;
	}
	
	public void testTabAvailable() throws PluginInitialisationException {
		// given
		controller.init(null, ctx);
		UiGeneratorController ui = mock(UiGeneratorController.class);
		FrontlineSMS frontlineController = mock(FrontlineSMS.class);
		when(ui.getFrontlineController()).thenReturn(frontlineController);
		when(frontlineController.getBean(anyString(), any(Class.class))).then(returnMockBean());
		inject(controller, "ctx", ctx);
		
		// expect
		assertNotNull(controller.getTab(ui));
	}

	public void testDeInitShutsDownScheduler() throws Exception {
		// given
		ScheduleHandler scheduleHandler = mock(ScheduleHandler.class);
		inject(controller, "scheduleHandler", scheduleHandler);
		inject(controller, "messageProcessor", learnIncomingMessageProcessor);
		
		// when
		controller.deinit();
		
		// then
		verify(scheduleHandler).shutdown();
	}
	
	public void testDeInitShutsDownMessageProcessor() {
		// given
		ScheduleHandler scheduleHandler = mock(ScheduleHandler.class);
		inject(controller, "scheduleHandler", scheduleHandler);
		inject(controller, "messageProcessor", learnIncomingMessageProcessor);
		
		// when
		controller.deinit();
		
		// then
		verify(learnIncomingMessageProcessor).shutdown();
	}
}

