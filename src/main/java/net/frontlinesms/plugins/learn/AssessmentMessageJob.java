package net.frontlinesms.plugins.learn;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AssessmentMessageJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			FrontlineSMS frontlineController = JobUtils.getFrontlineController(context);
			
			AssessmentMessage m = JobUtils.getAssessmentMessage(context);
			for(Contact c : JobUtils.getGroupMembers(context, m)) {
				System.out.println("AssessmentMessageJob.execute() : Sending message:");
				System.out.println("AssessmentMessageJob.execute() :     #:" + c.getPhoneNumber());
				System.out.println("AssessmentMessageJob.execute() :     t:" + m.getMessageText());
				System.out.println("AssessmentMessageJob.execute() :     f:" + m.getFrequency());
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
}
