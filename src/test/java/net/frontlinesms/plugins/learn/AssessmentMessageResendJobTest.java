package net.frontlinesms.plugins.learn;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;
import net.frontlinesms.test.spring.ApplicationContextAwareTestCase;
import net.frontlinesms.test.spring.MockBean;

import static org.mockito.Mockito.*;

public class AssessmentMessageResendJobTest extends ApplicationContextAwareTestCase {
	/** instance under test */
	private AssessmentMessageResendJob job;
	@Mock private JobExecutionContext context;
	@Mock private JobDataMap jobDataMap;
	@Mock private Scheduler scheduler;
	@Mock private SchedulerContext schedulerContext;
	@MockBean private AssessmentDao assessmentDao;
	@MockBean private AssessmentMessageDao assessmentMessageDao;
	@MockBean private AssessmentMessageResponseDao assessmentMessageResponseDao;
	@MockBean private GroupMembershipDao groupMembershipDao;
	@Mock private FrontlineSMS frontlineController;

	@Mock private AssessmentMessage m;
	@Mock private TopicItem topicItem;
	@Mock private Group group;
	@Mock private Assessment assessment;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		job = new AssessmentMessageResendJob();
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(context.getScheduler()).thenReturn(scheduler);
		when(scheduler.getContext()).thenReturn(schedulerContext);
		when(schedulerContext.get("applicationContext")).thenReturn(ctx);
		when(schedulerContext.get("frontlineController")).thenReturn(frontlineController);
	}
	
	public void testResendIsDoneIffNoResponseHasBeenReceived() throws JobExecutionException {
		// given
		when(jobDataMap.getLongValue("assessmentMessageId")).thenReturn(16L);
		when(m.getMessageText()).thenReturn("Got milk? Reply: 16TRUE or 16FALSE");
		when(m.getTopicItem()).thenReturn(topicItem);
		when(assessmentMessageDao.get(16)).thenReturn(m);
		List<Contact> mockContacts = mockContacts("+12345", "+67890");
		when(groupMembershipDao.getMembers(group)).thenReturn(mockContacts);
		when(assessmentDao.findByMessage(m)).thenReturn(assessment);
		when(assessment.getGroup()).thenReturn(group);

		when(assessmentMessageResponseDao.hasResponded(mockContacts.get(0), m)).thenReturn(true);
		when(assessmentMessageResponseDao.hasResponded(mockContacts.get(1), m)).thenReturn(false);
		
		// when
		job.execute(context);
		
		// then
		verify(frontlineController, never()).sendTextMessage("+12345", "Got milk? Reply: 16TRUE or 16FALSE");
		verify(frontlineController).sendTextMessage("+67890", "Got milk? Reply: 16TRUE or 16FALSE");
	}
	
	private List<Contact> mockContacts(String... phoneNumbers) {
		List<Contact> contacts = new ArrayList<Contact>();
		for(String p : phoneNumbers) {
			Contact c = mock(Contact.class);
			when(c.getPhoneNumber()).thenReturn(p);
			contacts.add(c);
		}
		return contacts;
	}
}
