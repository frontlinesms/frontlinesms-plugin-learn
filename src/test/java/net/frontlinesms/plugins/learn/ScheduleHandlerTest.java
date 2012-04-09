package net.frontlinesms.plugins.learn;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;

import static org.mockito.Mockito.*;

public class ScheduleHandlerTest extends BaseTestCase {
	/** instance under test */
	private ScheduleHandler handler;
	private EventBus eventBus;
	private AssessmentDao assessmentDao;
	private Scheduler scheduler;

	@Override
	protected void setUp() throws Exception {
		this.handler = new ScheduleHandler();
		
		this.eventBus = mock(EventBus.class);
		inject(handler, "eventBus", eventBus);
		
		this.assessmentDao = mock(AssessmentDao.class);
		inject(handler, "assessmentDao", assessmentDao);
		
		this.scheduler = mock(Scheduler.class);
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
}
