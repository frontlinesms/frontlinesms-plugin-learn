package net.frontlinesms.plugins.learn;

import org.mockito.Mock;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Reinforcement;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;

import static org.mockito.Mockito.*;
import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class ScheduleHandlerTest extends BaseTestCase {
	/** instance under test */
	private ScheduleHandler handler;
	@Mock private EventBus eventBus;
	@Mock private AssessmentDao assessmentDao;
	@Mock private Scheduler scheduler;
	private LearnPluginProperties properties = LearnPluginProperties.getInstance();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.handler = new ScheduleHandler();
		inject(handler, "eventBus", eventBus);
		inject(handler, "assessmentDao", assessmentDao);
		inject(handler, "scheduler", scheduler);
	}
	
	public void testStartShouldRegisterAsEventObserver() {
		// when
		handler.start();
		
		// then
		verify(eventBus).registerObserver(handler);
	}
	
	public void testStartupExceptionsShouldBePropogatedAsRuntimeException() throws Exception {
		// given
		doThrow(new SchedulerException()).when(scheduler).start();
		
		try {
			// when
			handler.start();
			
			fail("Exception should have been thrown.");
		} catch(RuntimeException ex) {
			// then
			pass("This exception is expected.");
		}
	}
	
	public void testShutdownShouldUnregisterAsEventObserver() {
		// when
		handler.shutdown();
		
		// then
		verify(eventBus).unregisterObserver(handler);
	}
	
	public void testSchedulerShutdownExceptionsAreNotPropogated() throws Exception {
		// given
		doThrow(new SchedulerException()).when(scheduler).shutdown();
		
		// when
		handler.shutdown();
		
		// then
		pass("No exception should have been thrown previously.");
	}
	
	public void testResendNotScheduledForQuestionIfResendsAreDisabled() throws Exception {
		// given
		properties.setResendDelay(0);
		properties.saveToDisk();
		
		// when
		handler.notify(createQuestionAmSavedNotification());
		
		// then
		verifyJobsScheduled(1);
	}
	
	public void testResendScheduledForQuestionIfResendsAreEnabled() throws Exception {
		// given
		properties.setResendDelay(3000);
		properties.saveToDisk();
		
		// when
		handler.notify(createQuestionAmSavedNotification());
		
		// then
		verifyJobsScheduled(2);
	}

	public void testResendNotScheduledForReinforcementIfResendsAreDisabled() throws Exception {
		// given
		properties.setResendDelay(0);
		properties.saveToDisk();
		
		// when
		handler.notify(createReinforcementAmSavedNotification());
		
		// then
		verifyJobsScheduled(1);
	}

	public void testResendNotScheduledForReinforcementIfResendsAreEnabled() throws Exception {
		// given
		properties.setResendDelay(3000);
		properties.saveToDisk();
		
		// when
		handler.notify(createReinforcementAmSavedNotification());
		
		// then
		verifyJobsScheduled(1);
	}

	private void verifyJobsScheduled(int invocationCount) throws Exception {
		verify(scheduler, times(invocationCount)).scheduleJob(any(JobDetail.class), any(Trigger.class));
	}
	
	private FrontlineEventNotification createQuestionAmSavedNotification() {
		AssessmentMessage m = mockAssessmentMessage(mock(Question.class), TODAY, TOMORROW);
		return mockEntitySavedNotification(m);
	}
	
	private FrontlineEventNotification createReinforcementAmSavedNotification() {
		AssessmentMessage m = mockAssessmentMessage(mock(Reinforcement.class), TODAY, TOMORROW);
		return mockEntitySavedNotification(m);
	}
}
