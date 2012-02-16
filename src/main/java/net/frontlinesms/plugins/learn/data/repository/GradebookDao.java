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

	public ClassTopicGradebook getForClassAndTopic(Group g, Topic t) {
		return null;
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