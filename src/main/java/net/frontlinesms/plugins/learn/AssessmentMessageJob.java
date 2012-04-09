package net.frontlinesms.plugins.learn;

import java.util.List;
import java.util.Map.Entry;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class AssessmentMessageJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			System.out.println("AssessmentMessageJob.execute() : ENTRY");
			
			JobDataMap data = context.getMergedJobDataMap();
			for(Entry<String, Object> e : data.entrySet()) {
				System.out.println("AssessmentMessageJob.execute() : \t" + e.getKey() + " -> " + e.getValue() + " (" + e.getValue().getClass() + ")");
			}
			
			long id = data.getLongValue("assessmentMessageId");
			System.out.println("AssessmentMessageJob.execute() : id=" + id);

			FrontlineSMS frontlineController = (FrontlineSMS) getSchedulerContext(context).get("frontlineController");
			
			AssessmentMessage m = getAssessmentMessageDao(context).get(id);
			for(Contact c : getGroupMembers(context, m)) {
				System.out.println("AssessmentMessageJob.execute() : Sending message:");
				System.out.println("AssessmentMessageJob.execute() :     #:" + c.getPhoneNumber());
				System.out.println("AssessmentMessageJob.execute() :     t:" + m.getMessageText());
				frontlineController.sendTextMessage(c.getPhoneNumber(), m.getMessageText());
			}
			
			System.out.println("AssessmentMessageJob.execute() : EXIT");
		} catch(Exception ex) {
			System.out.println("AssessmentMessageJob.execute() : caught exception: ");
			ex.printStackTrace();
			System.out.println("AssessmentMessageJob.execute() : rethrowing exception");
			throw new RuntimeException(ex);
		}
	}
	
	private List<Contact> getGroupMembers(JobExecutionContext context, AssessmentMessage m) throws SchedulerException {
		return getGroupMembershipDao(context).getMembers(getAssessmentDao(context).findByMessage(m).getGroup());
	}

	private GroupMembershipDao getGroupMembershipDao(JobExecutionContext context) throws SchedulerException {
		return (GroupMembershipDao) getApplicationContext(context).getBean("groupMembershipDao");
	}

	private AssessmentDao getAssessmentDao(JobExecutionContext context) throws BeansException, SchedulerException {
		return (AssessmentDao) getApplicationContext(context).getBean("assessmentDao");
	}
	
	private AssessmentMessageDao getAssessmentMessageDao(JobExecutionContext context) throws BeansException, SchedulerException {
		return (AssessmentMessageDao) getApplicationContext(context).getBean("assessmentMessageDao");
	}
	
	private ApplicationContext getApplicationContext(JobExecutionContext context) throws SchedulerException {
		return (ApplicationContext) getSchedulerContext(context).get("applicationContext");
	}

	private SchedulerContext getSchedulerContext(JobExecutionContext context) throws SchedulerException {
		return context.getScheduler().getContext();
	}
}
