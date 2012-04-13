package net.frontlinesms.plugins.learn;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.frontlinesms.FrontlineSMS;
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
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
	
	public ScheduleHandler(FrontlineSMS frontlineController, ApplicationContext ctx) throws PluginInitialisationException {
		eventBus = (EventBus) ctx.getBean("eventBus");
		assessmentDao = (AssessmentDao) ctx.getBean("assessmentDao");
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.getContext().put("applicationContext", ctx);
			scheduler.getContext().put("frontlineController", frontlineController);
		} catch (SchedulerException ex) {
			throw new PluginInitialisationException(ex);
		}

		System.out.println("ScheduleHandler.ScheduleHandler() : scheduler=" + scheduler);
	}
	
	public Scheduler getScheduler() {
		return scheduler;
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
			System.out.println("ScheduleHandler.start() : starting scheduler...");
			scheduler.start();
			System.out.println("ScheduleHandler.start() : scheduler started :¬)");
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
			} else if(entity instanceof Assessment) {
				for(AssessmentMessage m : ((Assessment) entity).getMessages()) {
					scheduleOrRescheduleJob(m);
				}
			}
		}
	}

//> SCHEDULE METHODS
	private void scheduleJob(AssessmentMessage m) {
		System.out.println("ScheduleHandler.scheduleJob() : " + m.getId());
		if(m.getEndDate() < System.currentTimeMillis()) return;
		try {
			scheduler.scheduleJob(createJob(m), createTrigger(m));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	private void scheduleOrRescheduleJob(AssessmentMessage m) {
		System.out.println("ScheduleHandler.scheduleOrRescheduleJob() : " + m.getId());
		Trigger trigger = null;
		try { trigger = scheduler.getTrigger(getTriggerKey(m)); } catch(SchedulerException ex) {
			ex.printStackTrace();
		}
		if(trigger == null) {
			scheduleJob(m);
		} else {
			rescheduleJob(m);
		}
	}

	private JobDetail createJob(AssessmentMessage m) {
		System.out.println("ScheduleHandler.createJob() : " + m.getId());
		return JobBuilder.newJob(AssessmentMessageJob.class)
				.usingJobData("assessmentMessageId", m.getId())
				.build();
	}

	private void rescheduleJob(AssessmentMessage m) {
		System.out.println("ScheduleHandler.rescheduleJob() : " + m.getId());
		try {
			scheduler.rescheduleJob(getTriggerKey(m), createTrigger(m));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	private void unscheduleJob(AssessmentMessage m) {
		System.out.println("ScheduleHandler.unscheduleJob() : " + m.getId());
		try {
			scheduler.unscheduleJob(getTriggerKey(m));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

//> TRIGGER BUILDING
	private TriggerKey getTriggerKey(AssessmentMessage m) {
		return new TriggerKey(m.getClass().getName() + m.getId());
	}

	private Trigger createTrigger(AssessmentMessage m) {
		System.out.println("ScheduleHandler.createTrigger() : Scheduling with frequency: " + m.getFrequency());
		String cronExp;
		switch(m.getFrequency()) {
			case ONCE:
				cronExp = "0 m H d M ? y";
				break;
			case DAILY:
				cronExp = "0 m H * * ? *";
				break;
			case WEEKLY:
				Calendar c = new GregorianCalendar();
				c.setTime(new Date());
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				cronExp = "0 m H ? * " + dayOfWeek + " *";
				break;
			case MONTHLY:
				cronExp = "0 m H d * ? *";
				break;
			default: throw new IllegalArgumentException("Cannot schedule at frequency: " + m.getFrequency());
		}

		String cronExpression = new SimpleDateFormat(cronExp).format(m.getStartDate());
		System.out.println("ScheduleHandler.createTrigger() : " + cronExpression);
		TriggerBuilder<CronTrigger> schedule = TriggerBuilder.newTrigger()
				.startNow()
				.withIdentity(getTriggerKey(m))
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
		if(!(m.getFrequency() == Frequency.ONCE)) {
			schedule = schedule.endAt(new Date(m.getEndDate()));
		}
		return schedule.build();
	}
}
