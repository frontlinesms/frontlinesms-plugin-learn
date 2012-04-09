package net.frontlinesms.plugins.learn;

import java.util.Map.Entry;

import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AssessmentMessageJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			System.out.println("AssessmentMessageJob.execute() : ENTRY");
			
			AssessmentMessageDao dao = (AssessmentMessageDao) context.getScheduler().getContext().get("applicationContext");
			
			System.out.println("AssessmentMessageJob.execute() : dao = " + dao);
			JobDataMap data = context.getMergedJobDataMap();
			for(Entry<String, Object> e : data.entrySet()) {
				System.out.println("AssessmentMessageJob.execute() : \t" + e.getKey() + " -> " + e.getValue() + " (" + e.getValue().getClass() + ")");
			}
			
			long id = data.getLongValue("assessmentMessageId");
			System.out.println("AssessmentMessageJob.execute() : id=" + id);
			
			System.out.println("AssessmentMessageJob.execute() : EXIT");
		} catch(Exception ex) {
			System.out.println("AssessmentMessageJob.execute() : caught exception: ");
			ex.printStackTrace();
			System.out.println("AssessmentMessageJob.execute() : rethrowing exception");
			throw new RuntimeException(ex);
		}
	}
}
