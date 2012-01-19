package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.*;

@Entity
public class Question extends TopicItem {
	public enum Type { BINARY, MULTIPLE_CHOICE }
	
	private String questionText;
	private Type type;
	private String[] answers;

//> ACCESSORS
	public String getQuestionText() {
		return questionText;
	}
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public String[] getAnswers() {
		return answers;
	}
	
	public void setAnswers(String... answers) {
		this.answers = answers;
	}
}