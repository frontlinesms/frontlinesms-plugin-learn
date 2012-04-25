package net.frontlinesms.plugins.learn;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AssessmentMessageResendJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			System.out.println("AssessmentMessageResendJob.execute() : ENTRY");
			
			FrontlineSMS frontlineController = JobUtils.getFrontlineController(context);
			
			AssessmentMessageResponseDao dao = (AssessmentMessageResponseDao) JobUtils.getApplicationContext(context).getBean("assessmentMessageResponseDao");
			
			AssessmentMessage m = JobUtils.getAssessmentMessage(context);
			for(Contact c : JobUtils.getGroupMembers(context, m)) {
				if(!dao.hasResponded(c, m)) {
					System.out.println("AssessmentMessageResendJob.execute() : Sending message:");
					System.out.println("AssessmentMessageResendJob.execute() :     #:" + c.getPhoneNumber());
					System.out.println("AssessmentMessageResendJob.execute() :     t:" + m.getMessageText());
					System.out.println("AssessmentMessageResendJob.execute() :     f:" + m.getFrequency());
					frontlineController.sendTextMessage(c.getPhoneNumber(), m.getMessageText());
				}
			}
			
			System.out.println("AssessmentMessageResendJob.execute() : EXIT");
		} catch(Exception ex) {
			System.out.println("AssessmentMessageResendJob.execute() : caught exception: ");
			ex.printStackTrace();
			System.out.println("AssessmentMessageResendJob.execute() : rethrowing exception");
			throw new RuntimeException(ex);
		}
	}
}
