package net.frontlinesms.plugins.learn.data.gradebook;

import java.util.List;

import net.frontlinesms.data.domain.Contact;

public class StudentGrades {
	private final Contact student;
	private final Integer[] grades;
	
	public StudentGrades(Contact student, List<Integer> grades) {
		this(student, grades.toArray(new Integer[grades.size()]));
	}
	
	public StudentGrades(Contact student, Integer[] grades) {
		this.student = student;
		this.grades = grades;
	}
	
	public Integer[] getGrades() {
		return grades;
	}
	
	public Contact getStudent() {
		return student;
	}

	public int getAverage() {
		int total = 0;
		for(Integer g : grades) {
			if(g != null) total += g;
		}
		return  (int) Math.round(1.0 * total / grades.length);
	}
}
