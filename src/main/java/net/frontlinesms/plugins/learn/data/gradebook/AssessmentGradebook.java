package net.frontlinesms.plugins.learn.data.gradebook;


public class AssessmentGradebook {
	private final int[] averages;
	private final StudentTopicResult[] results;
	
	public AssessmentGradebook(int[] averages, StudentTopicResult[] results) {
		this.averages = averages;
		this.results = results;
	}
	
	public int getQuestionCount() {
		return averages.length - 1;
	}
	
	public int[] getAverages() {
		return averages;
	}
	
	public StudentTopicResult[] getResults() {
		return results;
	}
}
