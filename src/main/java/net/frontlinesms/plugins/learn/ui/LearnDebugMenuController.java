package net.frontlinesms.plugins.learn.ui;

import java.util.ArrayList;
import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import net.frontlinesms.plugins.learn.ScheduleHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class LearnDebugMenuController implements ThinletUiEventHandler {
	private UiGeneratorController ui;
	private Scheduler scheduler;

	public void init(UiGeneratorController ui, ScheduleHandler scheduleHandler) {
		this.ui = ui;
		scheduler = scheduleHandler.getScheduler();
		
		Object debugMenu = ui.find("mnDebug");
		
		Object showJobsMenuItem = ui.createMenuitem(null, "Show scheduled jobs");
		ui.setAction(showJobsMenuItem, "debugSchedule", ui.getDesktop(), this);
		ui.add(debugMenu, showJobsMenuItem);
	}

	public void debugSchedule() throws SchedulerException {
		ArrayList<String> alert = new ArrayList<String>();
		for(String groupName : scheduler.getTriggerGroupNames()) {
			alert.add("TRIGGER GROUP: " + groupName);
			for(TriggerKey k : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
				Date nextFireTime = scheduler.getTrigger(k).getNextFireTime();
				String formatedDate = InternationalisationUtils.formatDate(nextFireTime);
				String formatedTime = InternationalisationUtils.formatTime(nextFireTime.getTime());
				alert.add(k + " -> " + formatedDate + " " + formatedTime);
			}
		}
		ui.alert(alert.toArray(new String[0]));
	}
}
