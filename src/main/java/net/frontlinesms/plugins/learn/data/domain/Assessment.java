package net.frontlinesms.plugins.learn.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	@ManyToOne
	private Topic topic;
	@ManyToOne
	private Group group;
	@OneToMany @OrderBy("startDate")
	private List<AssessmentMessage> messages = new ArrayList<AssessmentMessage>();
	private Long startDate;
	private Long endDate;
	
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
		long firstStartDate = Long.MAX_VALUE;
		long lastEndDate = -1;
		for(AssessmentMessage m : messages) {
			firstStartDate = Math.min(firstStartDate, m.getStartDate());
			Long endDate = m.getEndDate();
			if(endDate != null) lastEndDate = Math.max(lastEndDate, endDate);
		}
		this.startDate = firstStartDate == Long.MAX_VALUE? null: firstStartDate;
		this.endDate = lastEndDate == -1? null: lastEndDate;
		this.messages = messages;
	}
	
	public Long getStartDate() {
		return startDate;
	}
	
	public Long getEndDate() {
		return endDate;
	}
}