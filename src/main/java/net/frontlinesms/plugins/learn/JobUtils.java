package net.frontlinesms.plugins.learn;

import java.util.List;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class JobUtils {
	static List<Contact> getGroupMembers(JobExecutionContext context, AssessmentMessage m) throws SchedulerException {
		GroupMembershipDao groupMembershipDao = getGroupMembershipDao(context);
		AssessmentDao assessmentDao = getAssessmentDao(context);
		Assessment assessment = assessmentDao.findByMessage(m);
		Group group = assessment.getGroup();
		return groupMembershipDao.getMembers(group);
	}

	private static GroupMembershipDao getGroupMembershipDao(JobExecutionContext context) throws SchedulerException {
		return (GroupMembershipDao) getApplicationContext(context).getBean("groupMembershipDao");
	}

	private static AssessmentDao getAssessmentDao(JobExecutionContext context) throws BeansException, SchedulerException {
		return (AssessmentDao) getApplicationContext(context).getBean("assessmentDao");
	}
	
	static AssessmentMessageDao getAssessmentMessageDao(JobExecutionContext context) throws BeansException, SchedulerException {
		return (AssessmentMessageDao) getApplicationContext(context).getBean("assessmentMessageDao");
	}
	
	static ApplicationContext getApplicationContext(JobExecutionContext context) throws SchedulerException {
		return (ApplicationContext) getSchedulerContext(context).get("applicationContext");
	}

	static SchedulerContext getSchedulerContext(JobExecutionContext context) throws SchedulerException {
		return context.getScheduler().getContext();
	}

	public static AssessmentMessage getAssessmentMessage(JobExecutionContext context) throws BeansException, SchedulerException {
		System.out.println("JobUtils.getAssessmentMessage() : ENTRY");
		JobDataMap data = context.getMergedJobDataMap();
		
		long id = data.getLongValue("assessmentMessageId");
		System.out.println("JobUtils.getAssessmentMessage() : id=" + id);
		
		return JobUtils.getAssessmentMessageDao(context).get(id);
	}

	public static FrontlineSMS getFrontlineController(JobExecutionContext context) throws SchedulerException {
		return (FrontlineSMS) JobUtils.getSchedulerContext(context).get("frontlineController");
	}
}
