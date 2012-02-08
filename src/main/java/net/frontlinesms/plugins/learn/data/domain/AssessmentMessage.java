package net.frontlinesms.plugins.learn.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AssessmentMessage {
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	@ManyToOne
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

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
}
