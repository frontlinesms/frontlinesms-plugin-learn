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
	@SuppressWarnings("unused")
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
	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}
	
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	
	public long getEndDate() {
		if(frequency == Frequency.ONCE) return startDate;
		else return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
}
