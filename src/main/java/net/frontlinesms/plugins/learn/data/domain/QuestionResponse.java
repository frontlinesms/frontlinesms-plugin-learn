package net.frontlinesms.plugins.learn.data.domain;

public class QuestionResponse {
	public QuestionResponse(AssessmentMessage assessmentMessage, int answer) {
		assert assessmentMessage.getTopicItem() instanceof Question;
	}

	public boolean isCorrect() {
		return false;
	}
	
	public Integer getValue() {
		return 0;
	}
}
