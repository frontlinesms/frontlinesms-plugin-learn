package net.frontlinesms.plugins.learn.data.gradebook;

import java.util.List;

import net.frontlinesms.plugins.learn.data.domain.Topic;

/** Immutable class containing results at a specific point in time. */
public class ClassGradebook {
	private final List<Topic> topics;
	private final int[] topicAverages;
	private final List<StudentGrades> results;
	
	public ClassGradebook(List<Topic> topics, List<StudentGrades> results) {
		this.topics = topics;
		this.results = results;
		this.topicAverages = calculateTopicAverages();
	}

	private int[] calculateTopicAverages() {
		int[] topicAverages = new int[topics.size()];
		for (int topicIndex = 0; topicIndex < topicAverages.length; topicIndex++) {
			
		}
		return topicAverages;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public List<StudentGrades> getResults() {
		return results;
	}
	
	public int[] getTopicAverages() {
		return topicAverages;
	}
}
