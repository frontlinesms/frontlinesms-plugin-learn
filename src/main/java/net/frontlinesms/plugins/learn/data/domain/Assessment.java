package net.frontlinesms.plugins.learn.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import net.frontlinesms.data.domain.Group;

@Entity
public class Assessment implements HasTopic {
	/** Unique id for this entity.  This is for hibernate usage. */
	@SuppressWarnings("unused")
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	@ManyToOne
	private Topic topic;
	@ManyToOne
	private Group group;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER) @OrderBy("startDate")
	private List<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
	
	public Topic getTopic() {
		return topic;
	}
	
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setGroup(Group g) {
		group = g;
	}

	public Group getGroup() {
		return group;
	}
	
	public List<AssessmentMessage> getMessages() {
		return Collections.unmodifiableList(messages);
	}

	public void setMessages(List<AssessmentMessage> messages) {
		this.messages = messages;
	}
	
	public Long getStartDate() {
		long firstStartDate = Long.MAX_VALUE;
		for(AssessmentMessage m : messages) {
			firstStartDate = Math.min(firstStartDate, m.getStartDate());
		}
		return firstStartDate==Long.MAX_VALUE? null: firstStartDate;
	}
	
	public Long getEndDate() {
		long lastEndDate = -1;
		for(AssessmentMessage m : messages) {
			Long endDate = m.getEndDate();
			if(endDate != null) lastEndDate = Math.max(lastEndDate, endDate);
		}
		return lastEndDate==-1? null: lastEndDate;
	}

	public AssessmentMessage getMessage(TopicItem topicItem) {
		for(AssessmentMessage m : messages) {
			if(m.getTopicItem().equals(topicItem)) {
				return m;
			}
		}
		return null;
	}
}