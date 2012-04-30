package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.*;

@Entity
public class Question extends TopicItem {
	public enum Type { BINARY, MULTIPLE_CHOICE }
	
	private String questionText;
	private Type type;
	private String[] answers;
	private int correctAnswer;
	private String incorrectResponse;

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
	
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public String getIncorrectResponse() {
		return incorrectResponse;
	}
	
	public void setIncorrectResponse(String incorrectResponse) {
		this.incorrectResponse = incorrectResponse;
	}

//> FACTORY METHODS
	public static Question createBinary(Topic topic, String text, boolean correct) {
		Question q = new Question();
		q.setType(Type.BINARY);
		q.setCorrectAnswer(correct? 0: 1);
		q.setQuestionText(text);
		q.setMessageText(text);
		q.setTopic(topic);
		return q;
	}
	
	public static Question createMultichoice(Topic topic, String text, int correctAnswer, String... answers) {
		Question q = new Question();
		q.setType(Type.MULTIPLE_CHOICE);
		q.setCorrectAnswer(correctAnswer);
		q.setQuestionText(text);
		q.setMessageText(text);
		q.setAnswers(answers);
		q.setTopic(topic);
		return q;
	}
}