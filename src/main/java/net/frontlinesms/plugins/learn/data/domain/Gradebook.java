package net.frontlinesms.plugins.learn.data.domain;

import java.util.List;

/** Immutable class containing results at a specific point in time. */
public class Gradebook {
	private final List<Topic> topics;
	private final List<StudentGrades> results;
	
	public Gradebook(List<Topic> topics, List<StudentGrades> results) {
		this.topics = topics;
		this.results = results;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public List<StudentGrades> getResults() {
		return results;
	}
}
