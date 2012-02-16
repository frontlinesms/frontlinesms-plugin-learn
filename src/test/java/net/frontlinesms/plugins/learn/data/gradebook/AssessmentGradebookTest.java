package net.frontlinesms.plugins.learn.data.gradebook;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.*;
import net.frontlinesms.data.repository.*;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.learn.data.domain.*;
import net.frontlinesms.plugins.learn.data.repository.*;

import static net.frontlinesms.junit.BaseTestCase.*;

public class AssessmentGradebookTest extends HibernateTestCase {
	@Autowired private GradebookDao gradebookDao;
	@Autowired private AssessmentDao assessmentDao;
	@Autowired private GroupMembershipDao groupMembershipDao;
	@Autowired private GroupDao groupDao;
	@Autowired private ContactDao contactDao;
	@Autowired private QuestionDao questionDao;
	@Autowired private ReinforcementDao reinforcementDao;
	@Autowired private QuestionResponseDao questionResponseDao;
	
	public void testForEmptyClassWithEmptyAssessment() throws Exception {
		// given
		createClass(0);
		Assessment a = createAssessment(0, 0);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(0, g.getQuestionCount());
		assertEquals(array(0), g.getAverages());
		assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAReinforcement() throws Exception {
		// given
		createClass(0);
		Assessment a = createAssessment(0, 1);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(0, g.getQuestionCount());
		assertEquals(array(0), g.getAverages());
		assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAQuestion() throws Exception {
		// given
		createClass(0);
		Assessment a = createAssessment(1, 0);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(0, 0), g.getAverages());
		assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithAQuestionAndAReinforcement() throws Exception {
		// given
		createClass(0);
		Assessment a = createAssessment(1, 1);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(0, 0), g.getAverages());
		assertEquals(0, g.getResults().length);
	}

	public void testForEmptyClassWithAssessmentWithManyQuestionsAndReinforcements() throws Exception {
		// given
		createClass(0);
		Assessment a = createAssessment(3, 7);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(3, g.getQuestionCount());
		assertEquals(array(0, 0, 0, 0), g.getAverages());
		assertEquals(0, g.getResults().length);
	}

	public void testForSingleMemberClassWithEmptyAssessment() throws Exception {
		// given
		createClass(1);
		Assessment a = createAssessment(0, 0);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(0, g.getQuestionCount());
		assertEquals(array(0), g.getAverages());
		assertResults(g, array(0));
	}

	public void testForSingleMemberClassWithAssessmentWithAReinforcement() throws Exception {
		// given
		createClass(1);
		Assessment a = createAssessment(0, 1);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(0, g.getQuestionCount());
		assertEquals(array(0), g.getAverages());
		assertResults(g, array(0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestion() throws Exception {
		// given
		createClass(1);
		Assessment a = createAssessment(1, 0);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndAReinforcement() throws Exception {
		// given
		createClass(1);
		Assessment a = createAssessment(1, 1);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithManyQuestionsAndReinforcements() throws Exception {
		// given
		createClass(1);
		Assessment a = createAssessment(3, 7);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(3, g.getQuestionCount());
		assertEquals(array(0, 0, 0, 0), g.getAverages());
		assertResults(g, array(null, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndCorrectResponse() throws Exception {
		// given
		Contact c = getContacts(createClass(1))[0];
		Assessment a = createAssessment(1, 0);
		createResponse(c, a, 0, true);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(100, 100), g.getAverages());
		assertResults(g, array(1, 100));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndIncorrectResponse() throws Exception {
		// given
		Contact c = getContacts(createClass(1))[0];
		Assessment a = createAssessment(1, 0);
		createResponse(c, a, 0, false);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(0, 0), g.getAverages());
		assertResults(g, array(1, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithAQuestionAndAReinforcementAndAResponse() throws Exception {
		// given
		Contact c = getContacts(createClass(1))[0];
		Assessment a = createAssessment(1, 1);
		createResponse(c, a, 0, true);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(1, g.getQuestionCount());
		assertEquals(array(100, 100), g.getAverages());
		assertResults(g, array(1, 0));
	}

	public void testForSingleMemberClassWithAssessmentWithManyQuestionsAndReinforcementsAndSomeResponses() throws Exception {
		// given
		Contact c = getContacts(createClass(1))[0];
		Assessment a = createAssessment(3, 7);
		createResponse(c, a, 0, true);
		createResponse(c, a, 2, false);

		// when
		AssessmentGradebook g = gradebookDao.getForAssessment(a);
		
		// then
		assertEquals(3, g.getQuestionCount());
		assertEquals(array(100, 0, 0, 33), g.getAverages());
		assertResults(g, array(1, null, 1, 33));
	}
	
//> ASSERT METHODS
	private void assertResults(AssessmentGradebook g, Integer[]...results) {
	}
	
//> SETUP HELPER METHODS
	private Group createClass(int memberCount) throws Exception {
		Group g = new Group(null, "test-group");
		groupDao.saveGroup(g);
		
		for (int i = 0; i < memberCount; i++) {
			Contact c = new Contact("test-contact-"+i,
					"012345"+i, null, null, null, true);
			contactDao.saveContact(c);
		}
		
		return g;
	}
	
	private Contact[] getContacts(Group g) {
		return groupMembershipDao.getActiveMembers(g).toArray(new Contact[0]);
	}

	private Assessment createAssessment(int questionCount, int reinforcementCount) {
		Assessment a = new Assessment();
		
		for(int i=0; i<Math.max(questionCount, reinforcementCount); ++i) {
			if(i < questionCount) {
				Question q = new Question();
				questionDao.save(q);
			}
			if(i < reinforcementCount) {
				Reinforcement r = new Reinforcement();
				reinforcementDao.save(r);
			}
		}
		
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
		QuestionResponse r = new QuestionResponse(am, answer);
		questionResponseDao.save(r);
	}
}
