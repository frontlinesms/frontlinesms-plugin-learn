package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.frontlinesms.data.domain.Contact;

@Entity
public class AssessmentMessageResponse {
	/** Unique id for this entity.  This is for hibernate usage. */
	@SuppressWarnings("unused")
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	@ManyToOne(targetEntity=AssessmentMessage.class)
	private AssessmentMessage assessmentMessage;
	@ManyToOne(targetEntity=Contact.class)
	private Contact student;
	private boolean correct;
	private int answer;
	
	public AssessmentMessage getAssessmentMessage() {
		return assessmentMessage;
	}
	public void setAssessmentMessage(AssessmentMessage assessmentMessage) {
		this.assessmentMessage = assessmentMessage;
	}
	
	public Contact getStudent() {
		return student;
	}
	public void setStudent(Contact student) {
		this.student = student;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
}
