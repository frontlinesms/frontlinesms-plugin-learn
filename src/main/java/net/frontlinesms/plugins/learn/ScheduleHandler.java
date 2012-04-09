package net.frontlinesms.plugins.learn;

import java.util.Date;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;

import org.apache.log4j.Logger;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;

public class ScheduleHandler implements EventObserver {
	private final Logger log = FrontlineUtils.getLogger(getClass());
	private AssessmentDao assessmentDao;
	private EventBus eventBus;
	private Scheduler scheduler;
	
	ScheduleHandler() {}
	
	public ScheduleHandler(ApplicationContext ctx) throws PluginInitialisationException {
		eventBus = (EventBus) ctx.getBean("eventBus");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException ex) {
			throw new PluginInitialisationException(ex);
		}
	}
	
	public void start() {
		eventBus.registerObserver(this);
		try {
			// schedule jobs before starting...
			for(Assessment a : assessmentDao.list()) {
				for(AssessmentMessage m : a.getMessages()) {
					scheduleJob(m);
				}
			}
			scheduler.start();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}	
	}
	
	public void shutdown() {
		eventBus.unregisterObserver(this);
		try {
			scheduler.shutdown();
			scheduler = null;
		} catch (SchedulerException ex) {
			log.warn("Error shutting down scheduler.", ex);
			ex.printStackTrace();
		}
	}
	
//> EVENT HANDLING
	public void notify(FrontlineEventNotification n) {
		if(n instanceof DatabaseEntityNotification<?>) {
			Object entity = ((DatabaseEntityNotification<?>) n).getDatabaseEntity();
			if(entity instanceof AssessmentMessage) {
				AssessmentMessage m = (AssessmentMessage) entity;
				if(n instanceof EntitySavedNotification<?>) {
					scheduleJob(m);
				} else if(n instanceof EntityUpdatedNotification<?>) {
					rescheduleJob(m);
				} else if(n instanceof EntityDeletedNotification<?>) {
					unscheduleJob(m);
				}
			}
		}
	}
	
//> SCHEDULE METHODS
	private void scheduleJob(AssessmentMessage m) {
		try {
			scheduler.scheduleJob(createTrigger(m));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rescheduleJob(AssessmentMessage m) {
		try {
			scheduler.rescheduleJob(getTriggerKey(m), createTrigger(m));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void unscheduleJob(AssessmentMessage m) {
		try {
			scheduler.unscheduleJob(getTriggerKey(m));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//> TRIGGER BUILDING
	private TriggerKey getTriggerKey(AssessmentMessage m) {
		return new TriggerKey(m.getClass().getName() + m.getId());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Trigger createTrigger(AssessmentMessage m) {
		TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
				.startAt(new Date(m.getStartDate()))
				.withIdentity(getTriggerKey(m));
		switch(m.getFrequency()) {
			case ONCE:
				triggerBuilder = triggerBuilder
						.withSchedule(SimpleScheduleBuilder
								.repeatHourlyForTotalCount(1));
				break;
			case DAILY:
				triggerBuilder = triggerBuilder
						.withSchedule(DailyTimeIntervalScheduleBuilder
								.dailyTimeIntervalSchedule().onEveryDay())
								.endAt(new Date(m.getEndDate()));
				break;
			case WEEKLY:
				triggerBuilder = triggerBuilder
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInWeeks(1))
						.endAt(new Date(m.getEndDate()));
				break;
			case MONTHLY:
				triggerBuilder = triggerBuilder
						.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(1))
						.endAt(new Date(m.getEndDate()));
				break;
			default: throw new IllegalArgumentException("Cannot schedule at frequency: " + m.getFrequency());
		}
		return triggerBuilder.build();
	}
}
