package net.frontlinesms.plugins.learn.data.domain;

public class AssessmentMessage {
	private TopicItem topicItem;
	private Frequency frequency = Frequency.ONCE;
	private long startDate;
	private Long endDate;

//> CONSTRUCTORS
	public AssessmentMessage() {}

	public AssessmentMessage(TopicItem topicItem) {
		this.topicItem = topicItem;
	}

//> ACCESSORS
	public TopicItem getTopicItem() {
		return topicItem;
	}

	public Frequency getFrequency() {
		return frequency;
	}
	
	public long getStartDate() {
		return startDate;
	}
	
	public Long getEndDate() {
		return endDate;
	}
}
