package net.frontlinesms.plugins.learn.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import thinlet.Thinlet;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.plugins.learn.ScheduleHandler;
import net.frontlinesms.plugins.learn.data.domain.Assessment;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessage;
import net.frontlinesms.plugins.learn.data.domain.AssessmentMessageResponse;
import net.frontlinesms.plugins.learn.data.domain.Frequency;
import net.frontlinesms.plugins.learn.data.domain.Question;
import net.frontlinesms.plugins.learn.data.domain.Topic;
import net.frontlinesms.plugins.learn.data.domain.TopicItem;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.data.repository.TopicItemDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class LearnDebugMenuController implements ThinletUiEventHandler {
	private static final long THE_END_OF_TIME = Long.MAX_VALUE;
	
	private UiGeneratorController ui;
	private Scheduler scheduler;

	private ContactDao contactDao;
	private GroupDao groupDao;
	private GroupMembershipDao groupMembershipDao;
	private TopicDao topicDao;
	private TopicItemDao topicItemDao;
	private AssessmentDao assessmentDao;
	private AssessmentMessageResponseDao assessmentMessageResponseDao;
	private QuestionDao questionDao;

	public void init(UiGeneratorController ui, ScheduleHandler scheduleHandler) {
		this.ui = ui;
		scheduler = scheduleHandler.getScheduler();
		
		contactDao = ui.getFrontlineController().getContactDao();		
		groupDao = ui.getFrontlineController().getGroupDao();
		groupMembershipDao = ui.getFrontlineController().getBean("groupMembershipDao", GroupMembershipDao.class);
		topicDao = ui.getFrontlineController().getBean("topicDao", TopicDao.class);
		topicItemDao = ui.getFrontlineController().getBean("topicItemDao", TopicItemDao.class);
		assessmentDao = ui.getFrontlineController().getBean("assessmentDao", AssessmentDao.class);
		assessmentMessageResponseDao = ui.getFrontlineController().getBean("assessmentMessageResponseDao", AssessmentMessageResponseDao.class);
		questionDao = ui.getFrontlineController().getBean("questionDao", QuestionDao.class);

		ui.add(ui.find("mnDebug"), Thinlet.create(Thinlet.SEPARATOR));
		addMenuItem("Generate demo data", "generateDemoData");
		addMenuItem("Show scheduled jobs", "debugSchedule");
	}
	
	private void addMenuItem(String text, String method) {
		Object debugMenu = ui.find("mnDebug");
		
		Object showJobsMenuItem = ui.createMenuitem("/icons/logo_small.png", text);
		ui.setAction(showJobsMenuItem, method, ui.getDesktop(), this);
		ui.add(debugMenu, showJobsMenuItem);
	}
	
	public void generateDemoData() throws Exception {
		// create class
		Group g = createGroup("Happy Nurses");
		Contact[] contacts = createContacts("Nurse Peters", "Nurse Queztlcoatl", "Nurse Rerhyme");
		for(Contact c : contacts) groupMembershipDao.addMember(g, c);
		
		// create topic
		Topic t = createTopic("Nursing and Fun");
		createBinaryQuestion(t, "Patients feel best when they are happy!", true);
		createMultichoiceQuestion(t, "Nurses should wear:", "nothing below the elbow", "smiles", "all of the above");
				
		// create assessment
		Assessment a = createAssessment(g, t);
		
		// create responses
		int x = 0;
		for(AssessmentMessage m : a.getMessages()) {
			for(Contact c : contacts) {
				createResponse(c, m, ++x%2);
			}
		}
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
	
//> DATA SETUP METHODS
	private Contact[] createContacts(String... names) throws DuplicateKeyException {
		Contact[] contacts = new Contact[names.length];
		for (int i = 0; i < names.length; i++) {
			contacts[i] = new Contact(names[i] + System.currentTimeMillis(), "" + i +  + System.currentTimeMillis(), null, null, null, true);
			contactDao.saveContact(contacts[i]);
		}
		return contacts;
	}

	private Topic createTopic(String name) throws DuplicateKeyException {
		Topic t = new Topic();
		t.setName(name);
		topicDao.save(t);
		return t;
	}

	private AssessmentMessageResponse createResponse(Contact c, AssessmentMessage m, int i) {
		AssessmentMessageResponse r = new AssessmentMessageResponse();
		r.setStudent(c);
		r.setAssessmentMessage(m);
		r.setAnswer(i);
		r.setCorrect(i == ((Question) m.getTopicItem()).getCorrectAnswer());
		assessmentMessageResponseDao.save(r);
		return r;
	}

	private Assessment createAssessment(Group g, Topic t) {
		Assessment a = new Assessment();
		a.setTopic(t);
		a.setGroup(g);
		LinkedList<AssessmentMessage> messages = new LinkedList<AssessmentMessage>();
		for(TopicItem i : topicItemDao.getAllByTopic(t)) {
			AssessmentMessage m = new AssessmentMessage(i);
			m.setFrequency(Frequency.ONCE);
			m.setStartDate(THE_END_OF_TIME);
			messages.add(m);
		}
		a.setMessages(messages);
		assessmentDao.save(a);
		return a;
	}

	private Question createMultichoiceQuestion(Topic t, String question, String... answers) {
		Question q = Question.createMultichoice(t, question, 1, answers);
		questionDao.save(q);
		return q;
	}

	private Question createBinaryQuestion(Topic t, String string, boolean b) {
		Question q = Question.createBinary(t, string, b);
		questionDao.save(q);
		return q;
	}
	
	private Group createGroup(String name) throws DuplicateKeyException {
		Group g = new Group(new Group(null, null), name + System.currentTimeMillis());
		groupDao.saveGroup(g);
		return g;
	}
}
