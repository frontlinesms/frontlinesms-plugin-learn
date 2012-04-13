package net.frontlinesms.plugins.learn.data.gradebook;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.*;
import net.frontlinesms.data.repository.*;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;

import static net.frontlinesms.junit.BaseTestCase.*;

public class GradebookServiceTest extends HibernateTestCase {
	@Autowired private GradebookService gradebookService;
	
	@Autowired private AssessmentDao assessmentDao;
	@Autowired private GroupMembershipDao groupMembershipDao;
	@Autowired private GroupDao groupDao;
	@Autowired private ContactDao contactDao;
	@Autowired private QuestionDao questionDao;
	@Autowired private ReinforcementDao reinforcementDao;
	@Autowired private AssessmentMessageResponseDao assessmentMessageResponseDao;
	
	public void testForEmptyClassWithEmptyAssessment() throws Exception {
		// given
		Group group = createClass(0);
		Assessment a = createAssessment(group, 0, 0);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(0, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0), g.getAverages());
		BaseTestCase.assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAReinforcement() throws Exception {
		// given
		Group group = createClass(0);
		Assessment a = createAssessment(group, 0, 1);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(0, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0), g.getAverages());
		BaseTestCase.assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAQuestion() throws Exception {
		// given
		Group group = createClass(0);
		Assessment a = createAssessment(group, 1, 0);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0), g.getAverages());
		BaseTestCase.assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAQuestionAndAReinforcement() throws Exception {
		// given
		Group group = createClass(0);
		Assessment a = createAssessment(group, 1, 1);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0), g.getAverages());
		BaseTestCase.assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithManyQuestionsAndReinforcements() throws Exception {
		// given
		Group group = createClass(0);
		Assessment a = createAssessment(group, 3, 7);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(3, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0, 0, 0), g.getAverages());
		BaseTestCase.assertEquals(0, g.getResults().length);
	}

	public void testForSingleMemberClassWithEmptyAssessment() throws Exception {
		// given
		Group group = createClass(1);
		Assessment a = createAssessment(group, 0, 0);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(0, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0), g.getAverages());
		assertResults(g, 0);
	}

	public void testForSingleMemberClassWithAssessmentWithAReinforcement() throws Exception {
		// given
		Group group = createClass(1);
		Assessment a = createAssessment(group, 0, 1);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(0, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0), g.getAverages());
		assertResults(g, 0);
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestion() throws Exception {
		// given
		Group group = createClass(1);
		Assessment a = createAssessment(group, 1, 0);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndAReinforcement() throws Exception {
		// given
		Group group = createClass(1);
		Assessment a = createAssessment(group, 1, 1);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithManyQuestionsAndReinforcements() throws Exception {
		// given
		Group group = createClass(1);
		Assessment a = createAssessment(group, 3, 7);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(3, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0, 0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndCorrectResponse() throws Exception {
		// given
		Group group = createClass(1);
		Contact c = getContacts(group)[0];
		Assessment a = createAssessment(group, 1, 0);
		createResponse(c, a, 0, true);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(100, 100), g.getAverages());
		assertResults(g, 0, 100);
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndIncorrectResponse() throws Exception {
		// given
		Group group = createClass(1);
		Contact c = getContacts(group)[0];
		Assessment a = createAssessment(group, 1, 0);
		createResponse(c, a, 0, false);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(0, 0), g.getAverages());
		assertResults(g, 1, 0);
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndAReinforcementAndAResponse() throws Exception {
		// given
		Group group = createClass(1);
		Contact c = getContacts(group)[0];
		Assessment a = createAssessment(group, 1, 1);
		createResponse(c, a, 0, true);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(1, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(100, 100), g.getAverages());
		assertResults(g, 0, 100);
	}

	public void testForSingleMemberClassWithAssessmentWithManyQuestionsAndReinforcementsAndSomeResponses() throws Exception {
		// given
		Group group = createClass(1);
		Contact c = getContacts(group)[0];
		Assessment a = createAssessment(group, 3, 7);
		createResponse(c, a, 0, true);
		createResponse(c, a, 2, false);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(3, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(100, 0, 0, 33), g.getAverages());
		assertResults(g, array(0, null, 1, 33));
	}

	public void testForMultiMemberClassWithAssessmentWithManyQuestionsAndReinforcementsAndSomeResponses() throws Exception {
		// given
		Group group = createClass(2);
		Assessment a = createAssessment(group, 3, 7);

		Contact c0 = getContacts(group)[0];
		createResponse(c0, a, 0, true);
		createResponse(c0, a, 2, false);

		Contact c1 = getContacts(group)[1];
		createResponse(c1, a, 1, false);
		createResponse(c1, a, 2, false);

		// when
		AssessmentGradebook g = gradebookService.getForAssessment(a);
		
		// then
		BaseTestCase.assertEquals(3, g.getQuestionCount());
		BaseTestCase.assertEquals("averages", array(50, 0, 0, 16), g.getAverages());
		assertResults(g,
				objectArray(0, null, 1, 33),
				objectArray(null, 1, 1, 0));
	}
	
//> ASSERT METHODS
	private void assertResults(AssessmentGradebook g, Integer... results) {
		assertResults(g, new Integer[][] { results });
	}
	
	private void assertResults(AssessmentGradebook g, Integer[]...results) {
		StudentTopicResult[] strs = g.getResults();
		assertEquals("results length", results.length, strs.length);
		for(int i=0; i<results.length; ++i) {
			StudentTopicResult str = strs[i];
			Integer[] individualResults = results[i];
			for (int j = 0; j < individualResults.length-1; j++) {
				AssessmentMessageResponse amr = str.getResponses()[j];
				Integer answer = amr==null? null: amr.getAnswer();
				assertEquals("result #"+i, individualResults[j], answer);
			}
			assertEquals(individualResults[individualResults.length-1].intValue(), str.getScore());
		}
	}
	
//> SETUP HELPER METHODS
	private Group createClass(int memberCount) throws Exception {
		Group g = new Group(new Group(null, null), "test-group");
		groupDao.saveGroup(g);
		
		for (int i = 0; i < memberCount; i++) {
			Contact c = new Contact("test-contact-"+i,
					"012345"+i, null, null, null, true);
			contactDao.saveContact(c);
			groupMembershipDao.addMember(g, c);
		}
		
		return g;
	}
	
	private Contact[] getContacts(Group g) {
		return groupMembershipDao.getActiveMembers(g).toArray(new Contact[0]);
	}

	private Assessment createAssessment(Group g, int questionCount, int reinforcementCount) {
		Assessment a = new Assessment();
		a.setGroup(g);
		
		List<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
		for(int i=0; i<Math.max(questionCount, reinforcementCount); ++i) {
			if(i < questionCount) {
				Question q = new Question();
				q.setAnswers(new String[3]);
				questionDao.save(q);
				messages.add(new AssessmentMessage(q));
			}
			if(i < reinforcementCount) {
				Reinforcement r = new Reinforcement();
				reinforcementDao.save(r);
				messages.add(new AssessmentMessage(r));
			}
		}
		a.setMessages(messages);
		
		assessmentDao.save(a);
		
		return a;
	}
	
	private void createResponse(Contact c, Assessment a, int questionNumber, boolean correct) {
		AssessmentMessage am = null;
		for(AssessmentMessage m : a.getMessages()) {
			if(m.getTopicItem() instanceof Question) {
				if(--questionNumber < 0) {
					am = m;
					break;
				}
			}
		}
		if(am == null) throw new RuntimeException("Could not find question #" + questionNumber);
		
		Question q = (Question) am.getTopicItem();
		int answer = correct? q.getCorrectAnswer(): q.getCorrectAnswer()+1 % q.getAnswers().length;
		AssessmentMessageResponse r = new AssessmentMessageResponse();
		r.setStudent(c);
		r.setAnswer(answer);
		r.setAssessmentMessage(am);
		r.setCorrect(correct);
		assessmentMessageResponseDao.save(r);
	}
}
