package net.frontlinesms.plugins.learn.data.gradebook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.domain.Question.Type;
import net.frontlinesms.plugins.learn.data.repository.AssessmentDao;
import net.frontlinesms.plugins.learn.data.repository.AssessmentMessageResponseDao;
import net.frontlinesms.plugins.learn.data.repository.GradebookDao;
import net.frontlinesms.plugins.learn.data.repository.QuestionDao;
import net.frontlinesms.plugins.learn.data.repository.ReinforcementDao;
import net.frontlinesms.plugins.learn.data.repository.TopicDao;
import net.frontlinesms.plugins.learn.data.repository.TopicItemDao;

import static net.frontlinesms.plugins.learn.LearnTestUtils.*;

public class ClassGradebookTest extends HibernateTestCase {
	@Autowired private GradebookDao gradebookDao;
	@Autowired private AssessmentDao assessmentDao;
	@Autowired private AssessmentMessageResponseDao assessmentMessageResponseDao;
	@Autowired private ContactDao contactDao;
	@Autowired private GroupDao groupDao;
	@Autowired private GroupMembershipDao groupMembershipDao;
	@Autowired private QuestionDao questionDao;
	@Autowired private ReinforcementDao reinforcementDao;
	@Autowired private TopicDao topicDao;
	@Autowired private TopicItemDao topicItemDao;
	
	public void testGradebookForEmptyClassWithoutAssessments() throws Exception {
		// given
		Group g = createGroup();
		
		// when
		ClassGradebook gb = gradebookDao.getForClass(g);
		
		// then
		assertEquals(0, gb.getResults().size());
		assertEquals(0, gb.getTopics().size());
	}
	
	public void testGradebookForEmptyClassWithAssessments() throws Exception {
		// given
		Group g = createGroup();
		createAssessments(g, 2);
		
		// when
		ClassGradebook gb = gradebookDao.getForClass(g);
		
		// then
		assertEquals(0, gb.getResults().size());
		assertEquals(2, gb.getTopics().size());
	}
	
	public void testGradebookForClassWithNoAssessments() throws Exception {
		// given
		Group g = createGroup();
		createContacts(g, 3);
		
		// when
		ClassGradebook gb = gradebookDao.getForClass(g);
		
		// then
		assertEquals(3, gb.getResults().size());
		assertEquals(0, gb.getTopics().size());
	}
	
	public void testGradebookForClassWithAssessmentButNoResponses() throws Exception {
		// given
		Group g = createGroup();
		createContacts(g, 3);
		assertEquals(3, groupMembershipDao.getMemberCount(g));
		createAssessments(g, 2);
		
		// when
		ClassGradebook gb = gradebookDao.getForClass(g);
		
		// then
		assertEquals(3, gb.getResults().size());
		Integer[] actualGrades = gb.getResults().get(0).getGrades();
		assertEquals(2, actualGrades.length);
		Integer[] expectedGrades = {null, null};
		BaseTestCase.assertEquals("first student's grades", expectedGrades, actualGrades);
		assertEquals(2, gb.getTopics().size());
	}
	
	public void testGradebookForClassWithAssessmentAndResponses() throws Exception {
		// given
		Group g = createGroup();
		Contact[] contacts = createContacts(g, 3);
		Assessment assessment = createAssessment(g, "some exciting topic");
		createResponses(assessment, contacts);

		// when
		List<StudentGrades> actualGrades = gradebookDao.getForClass(g).getResults();
		
		// then
		assertEquals(3, actualGrades.size());
		Integer[][] expectedGrades = {{100}, {0}, {50}};
		BaseTestCase.assertEquals("contact 0's grades", expectedGrades[0], actualGrades.get(0).getGrades());
		BaseTestCase.assertEquals("contact 1's grades", expectedGrades[1], actualGrades.get(1).getGrades());
		BaseTestCase.assertEquals("contact 2's grades", expectedGrades[2], actualGrades.get(2).getGrades());
	}
	
	public void testGradebookForClassWithMultipleAssessmentsAndResponsesButSomeResponsesMissing() throws Exception {
		// given
		Group g = createGroup();
		Contact[] contacts = createContacts(g, 3);
		Assessment[] assessments = createAssessments(g, 2);
		createResponses(assessments[0], contacts);
		createResponses(assessments[1], contacts[0], contacts[2]);
		
		// when
		List<StudentGrades> actualGrades = gradebookDao.getForClass(g).getResults();
		
		// then
		Integer[][] expectedGrades = {
				/* contact 0 */ {100, 100},
				/* contact 1 */ {0, null},
				/* contact 2 */ {50, 0}
		};
		BaseTestCase.assertEquals("contact 0's grades", expectedGrades[0], actualGrades.get(0).getGrades());
		BaseTestCase.assertEquals("contact 0's grades", expectedGrades[1], actualGrades.get(1).getGrades());
		BaseTestCase.assertEquals("contact 0's grades", expectedGrades[2], actualGrades.get(2).getGrades());
	}
	
//> TEST SETUP METHODS
	private Topic createTopic(String topicName) throws Exception {
		Topic t = new Topic();
		t.setName(topicName);
		topicDao.save(t);
		
		createTopicItems(t);

		return t;
	}
	
	private void createTopicItems(Topic t) {
		Reinforcement r = new Reinforcement();
		r.setTopic(t);
		reinforcementDao.save(r);
		
		Question binaryQ = new Question();
		binaryQ.setType(Type.BINARY);
		binaryQ.setTopic(t);
		questionDao.save(binaryQ);
		
		Question multichoiceQ = new Question();
		multichoiceQ.setType(Type.MULTIPLE_CHOICE);
		multichoiceQ.setTopic(t);
		questionDao.save(multichoiceQ);
	}

	private void createResponses(Assessment a, Contact... students) {
		int count = 0;
		for(Contact s : students) {
			for(AssessmentMessage m : a.getMessages()) {
				if(m.getTopicItem() instanceof Question) {
					AssessmentMessageResponse r = new AssessmentMessageResponse();
					r.setAssessmentMessage(m);
					r.setStudent(s);
					boolean binary = ((Question) m.getTopicItem()).getType() == Type.BINARY;
					int countess = count % (binary ? 2 : 3);
					boolean correct = countess == 0;
					r.setCorrect(correct);
					r.setAnswer(countess);
					assessmentMessageResponseDao.save(r);
				}
			}
			++count;
		}
	}
	
	private Group createGroup() throws Exception {
		Group g = new Group(new Group(null, null), "test-class");
		groupDao.saveGroup(g);
		return g;
	}
	
	private Assessment createAssessment(Group g, String topicName) throws Exception {
		Assessment a = new Assessment();
		Topic topic = createTopic(topicName);
		a.setTopic(topic);
		a.setGroup(g);
		a.setMessages(createAssessmentMessages(topic));
		assessmentDao.save(a);
		return a;
	}
	
	private List<AssessmentMessage> createAssessmentMessages(Topic topic) {
		ArrayList<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
		for(TopicItem i : topicItemDao.getAllByTopic(topic)) {
			AssessmentMessage assessmentMessage = new AssessmentMessage(i);
			assessmentMessage.setStartDate(YESTERDAY);
			assessmentMessage.setEndDate(TOMORROW);
			messages.add(assessmentMessage);
		}
		return messages;
	}

	private Assessment[] createAssessments(Group g, int count) throws Exception {
		Assessment[] assessments = new Assessment[count];
		for (int i = 0; i < count; i++) {
			assessments[i] = createAssessment(g, "topic-" + i);
		}
		return assessments;
	}
	
	private Contact[] createContacts(Group g, int count) throws Exception {
		Contact[] c = new Contact[count];
		for (int i = 0; i < count; i++) {
			c[i] = new Contact("name-" + i, "" + i, null, null, null, true);
			contactDao.saveContact(c[i]);
			groupMembershipDao.addMember(g, c[i]);
		}
		return c;
	}
}
