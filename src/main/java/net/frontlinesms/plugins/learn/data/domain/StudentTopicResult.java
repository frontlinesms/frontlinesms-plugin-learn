package net.frontlinesms.plugins.learn.data.domain;

import net.frontlinesms.data.domain.Contact;

public class StudentTopicResult {
	private final Contact contact;
	private final AssessmentMessageResponse[] responses;
	
	public StudentTopicResult(Contact contact,
			AssessmentMessageResponse[] responses) {
		this.contact = contact;
		this.responses = responses;
	}

	public Contact getContact() {
		return contact;
	}

	public AssessmentMessageResponse[] getResponses() {
		return responses;
	}
	
	public int getScore() {
		return responses.length > 0?
				100 * getCorrectCount() / responses.length: 0;
	}
	
	private int getCorrectCount() {
		int correctCount = 0;
		for(AssessmentMessageResponse r : responses) {
			if(r!=null && r.isCorrect()) ++correctCount;
		}
		return correctCount;
	}
}
