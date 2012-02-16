package net.frontlinesms.plugins.learn.data.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.plugins.learn.data.domain.*;

public class GradebookDao {
//> DATA ACCESS OBJECTS
	@Autowired private AssessmentDao assessmentDao;
	@Autowired private AssessmentMessageResponseDao assessmentMessageResponseDao;
	@Autowired private GroupMembershipDao groupMembershipDao;

//> FETCHERS
	public ClassGradebook getForClass(Group g) {
		List<Assessment> assessments = assessmentDao.findAllByGroup(g);
		List<Topic> topics = new ArrayList<Topic>();
		for(Assessment a : assessments) {
			topics.add(a.getTopic());
		}

		List<StudentGrades> results = new ArrayList<StudentGrades>();
		List<Contact> students = groupMembershipDao.getMembers(g);
		Collections.sort(students, new StudentSorter());
		for(Contact student : students) {
			results.add(createResult(student, assessments));
		}
		return new ClassGradebook(topics, results);
	}

	public AssessmentGradebook getForAssessment(Assessment a) {
		List<Question> questions = getQuestions(a);
		int questionCount = questions.size();
		
		Group group = a.getGroup();
		List<Contact> students = groupMembershipDao.getActiveMembers(group);
		int studentCount = students.size();
		StudentTopicResult[] results = new StudentTopicResult[studentCount];
		for (int i = 0; i < results.length; i++) {
			Contact student = students.get(i);
			List<AssessmentMessageResponse> amrs = assessmentMessageResponseDao.findAllByStudentAndAssessment(student, a);
			AssessmentMessageResponse[] orderedResponses = new AssessmentMessageResponse[questionCount];
			for(AssessmentMessageResponse amr : amrs) {
				orderedResponses[questions.indexOf(amr.getAssessmentMessage().getTopicItem())] = amr;
			}
			StudentTopicResult str = new StudentTopicResult(student, orderedResponses);
			results[i] = str;
		}
		
		// calculate averages
		int[] averages = new int[questionCount + 1];
		int totalCorrect = 0;
		for(int i=0; i<questionCount; ++i) {
			if(studentCount > 0) {
				int correctCount = 0;
				for(StudentTopicResult r : results) {
					AssessmentMessageResponse amr = r.getResponses()[i];
					if(amr!=null && amr.isCorrect()) ++correctCount;
				}
				averages[i] = correctCount * 100 / studentCount;
				totalCorrect += correctCount;
			}
		}
		if(studentCount * questionCount > 0) {
			averages[averages.length - 1] = totalCorrect * 100 / (studentCount * questionCount);
		}
		
		return new AssessmentGradebook(averages, results);
	}

//> HELPERS
	private StudentGrades createResult(Contact student,
			List<Assessment> assessments) {
		List<Integer> grades = new ArrayList<Integer>();
		for(Assessment a : assessments) {
			List<AssessmentMessageResponse> responses =
					assessmentMessageResponseDao.findAllByStudentAndAssessment(student, a);
			int totalQuestions = getTotalQuestions(a);
			if(responses.size() == 0) {
				grades.add(null);
			} else {
				int correct = countCorrect(responses);
				grades.add(correct * 100 / totalQuestions);
			}
		}
		return new StudentGrades(student, grades);
	}

	private int countCorrect(List<AssessmentMessageResponse> responses) {
		int count = 0;
		for (AssessmentMessageResponse r : responses) {
			if(r.isCorrect()) ++count;
		}
		return count;
	}
	
	private List<Question> getQuestions(Assessment a) {
		List<Question> questions = new ArrayList<Question>();
		for(AssessmentMessage m : a.getMessages()) {
			if(m.getTopicItem() instanceof Question) {
				questions.add((Question) m.getTopicItem());
			}
		}
		return questions;
	}

	private int getTotalQuestions(Assessment a) {
		int count = 0;
		for(AssessmentMessage m : a.getMessages()) {
			if(m.getTopicItem() instanceof Question) ++count;
		}
		return count;
	}
}

class StudentSorter implements Comparator<Contact> {
	public int compare(Contact c1, Contact c2) {
		return c1.getName().compareTo(c2.getName());
	}
}