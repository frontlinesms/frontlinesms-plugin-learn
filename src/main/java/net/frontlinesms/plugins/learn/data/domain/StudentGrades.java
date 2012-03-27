package net.frontlinesms.plugins.learn.data.domain;

import java.util.List;

import net.frontlinesms.data.domain.Contact;

public class StudentGrades {
	private final Contact student;
	private final Integer[] grades;
	
	public StudentGrades(Contact student, List<Integer> grades) {
		this.student = student;
		this.grades = grades.toArray(new Integer[grades.size()]);
	}
	
	public Integer[] getGrades() {
		return grades;
	}
	
	public Contact getStudent() {
		return student;
	}

	public int getAverage() {
		return 3;
	}
}
